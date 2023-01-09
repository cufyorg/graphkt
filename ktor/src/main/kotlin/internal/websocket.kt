/*
 *	Copyright 2022 cufy.org
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.cufy.graphkt.ktor.internal

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.collections.*
import io.ktor.websocket.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.GraphQLPacket
import org.cufy.graphkt.schema.GraphQLPacketType
import org.cufy.graphkt.schema.GraphQLRequest
import org.cufy.graphkt.schema.GraphQLResponse
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration

internal object GraphQLLegacyPacketType {
    /**
     * The legacy equivalent to [GraphQLPacketType.Subscribe].
     */
    const val Start = "start"

    /**
     * The legacy equivalent to [GraphQLPacketType.Next].
     */
    const val Data = "data"

    /**
     * The legacy equivalent to [GraphQLPacketType.Complete].
     */
    const val Stop = "stop"
}

internal val ConnectionInitialisationTimeout =
    CloseReason(4408, "Connection initialisation timeout")

internal val TooManyInitialisationRequests =
    CloseReason(4429, "Too many initialisation requests")

internal val Unauthorized =
    CloseReason(4401, "Unauthorized")

@Suppress("FunctionName")
internal fun InvalidMessage(errorMessage: String) =
    CloseReason(4400, errorMessage)

@Suppress("FunctionName")
internal fun SubscriberAlreadyExists(uniqueOperationId: String) =
    CloseReason(4409, "Subscriber for $uniqueOperationId already exists")

/**
 * Add a websocket route handling graphql websocket
 * frames.
 *
 * This is the actual websocket implementation.
 *
 * This implementation follows [`graphql-ws`](https://github.com/enisdenjo/graphql-ws/blob/master/PROTOCOL.md)
 * specifications.
 *
 * @param path the route path.
 * @param handler a graphql request handler.
 */
@InternalGraphktApi
internal fun Route.graphqlWebsocket(
    path: String,
    connectionInitWaitTimeout: Duration?,
    handler: suspend DefaultWebSocketServerSession.(GraphQLRequest) -> Flow<GraphQLResponse>
) {
    /*
    This implementation is optimistic.
    No defences nor validation has been added, yet.
    */

    webSocket(path) {
        val initialized = AtomicBoolean(false)
        val subscriptions: MutableMap<String, Job> = ConcurrentHashMap()

        if (connectionInitWaitTimeout != null) {
            launch {
                delay(connectionInitWaitTimeout)

                if (!initialized.get()) {
                    close(ConnectionInitialisationTimeout)
                }
            }
        }

        for (frame in incoming) {
            val message = frame.readBytes().decodeToString()
            val data = GraphktKtorJson.decodeFromString<GraphQLPacket>(message)

            when (data.type) {
                GraphQLPacketType.ConnectionInit -> {
                    if (initialized.get()) {
                        close(TooManyInitialisationRequests)
                        return@webSocket
                    }

                    initialized.set(true)

                    val rData = GraphQLPacket(data.id, GraphQLPacketType.ConnectionAck)
                    val rMessage = GraphktKtorJson.encodeToString(rData)

                    send(rMessage)
                }
                GraphQLPacketType.Ping -> {
                    val rData = GraphQLPacket(data.id, GraphQLPacketType.Pong)
                    val rMessage = GraphktKtorJson.encodeToString(rData)

                    send(rMessage)
                }
                GraphQLPacketType.Subscribe,
                GraphQLLegacyPacketType.Start -> {
                    val rType = when (data.type) {
                        GraphQLPacketType.Subscribe -> GraphQLPacketType.Next
                        GraphQLLegacyPacketType.Start -> GraphQLLegacyPacketType.Data
                        else -> error("internal error")
                    }

                    val id = data.id ?: run {
                        close(InvalidMessage("id is required for packet type: ${data.type}"))
                        return@webSocket
                    }

                    if (id in subscriptions) {
                        close(SubscriberAlreadyExists(id))
                        return@webSocket
                    }

                    if (!initialized.get()) {
                        close(Unauthorized)
                    }

                    val payload = data.payload ?: run {
                        close(InvalidMessage("payload is required for packet type: ${data.type}"))
                        return@webSocket
                    }

                    val request = GraphktKtorJson.decodeFromJsonElement<GraphQLRequest>(payload)
                    val response = handler(request)

                    val job = launch {
                        response.collect {
                            val rPayload = GraphktKtorJson.encodeToJsonElement(it).jsonObject
                            val rData = GraphQLPacket(data.id, rType, rPayload)
                            val rMessage = GraphktKtorJson.encodeToString(rData)

                            send(rMessage)
                        }
                        response.onCompletion {
                            val rData = GraphQLPacket(data.id, GraphQLPacketType.Complete)
                            val rMessage = GraphktKtorJson.encodeToString(rData)

                            send(rMessage)
                        }
                    }

                    subscriptions[id] = job
                }
                GraphQLPacketType.Complete,
                GraphQLLegacyPacketType.Stop -> {
                    val id = data.id ?: run {
                        close(InvalidMessage("id is required for packet type: ${data.type}"))
                        return@webSocket
                    }

                    subscriptions[id]?.cancel()

                    // extra stuff for stop
                    if (data.type == GraphQLLegacyPacketType.Stop) {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Ok"))
                    }
                }
                else -> {
                    close(InvalidMessage("Unexpected packet type: ${data.type}"))
                }
            }
        }
    }
}
