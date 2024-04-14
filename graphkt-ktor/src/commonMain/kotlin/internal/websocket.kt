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
import io.ktor.websocket.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.cufy.graphkt.ktor.GraphQLKtorConfiguration
import org.cufy.graphkt.schema.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.set

private val Close_ConnectionInitialisationTimeout =
    CloseReason(4408, "Connection initialisation timeout")

private val Close_TooManyInitialisationRequests =
    CloseReason(4429, "Too many initialisation requests")

private val Close_Unauthorized =
    CloseReason(4401, "Unauthorized")

private val Close_InvalidMessage_PacketIdRequired =
    CloseReason(4400, "packet.id is required for specified packet.type")

private val Close_InvalidMessage_PacketPayloadRequired =
    CloseReason(4400, "packet.payload is required for specified packet.type")

private val Close_InvalidMessage_PacketTypeUnexpected =
    CloseReason(4400, "Unexpected packet.type")

private val Close_SubscriberAlreadyExists =
    CloseReason(4409, "Subscriber already exists")

private val Close_Normal =
    CloseReason(CloseReason.Codes.NORMAL, "Ok")

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
internal fun Route.graphqlWebsocket(
    path: String,
    configuration: GraphQLKtorConfiguration,
    handler: suspend DefaultWebSocketServerSession.(GraphQLRequest) -> Flow<GraphQLResponse>
) {
    /*
    This implementation is optimistic.
    No defences nor validation has been added, yet.
    */

    val connectionInitWaitTimeout = configuration.connectionInitWaitTimeout

    webSocket(path) {
        val sessionInitialized = AtomicBoolean(false)
        val sessionSubscriptions: MutableMap<String, Job> = ConcurrentHashMap()
        val sessionLegacySubscriptions: MutableMap<String, Job> = ConcurrentHashMap()

        if (connectionInitWaitTimeout != null) {
            launch {
                delay(connectionInitWaitTimeout)

                if (!sessionInitialized.get()) {
                    close(Close_ConnectionInitialisationTimeout)
                }
            }
        }

        for (frame in incoming) {
            val frameInPacketString = frame.readBytes().decodeToString()
            val frameInPacket = GraphktKtorJson.decodeFromString<GraphQLPacket>(frameInPacketString)

            when (frameInPacket.type) {
                GraphQLPacketType.ConnectionInit -> {
                    if (sessionInitialized.get()) {
                        close(Close_TooManyInitialisationRequests)
                        return@webSocket
                    }

                    sessionInitialized.set(true)

                    val frameOutPacket = GraphQLPacket(frameInPacket.id, GraphQLPacketType.ConnectionAck)
                    val frameOutMessageString = GraphktKtorJson.encodeToString(frameOutPacket)

                    send(frameOutMessageString)
                }

                GraphQLPacketType.Ping -> {
                    val frameOutPacket = GraphQLPacket(frameInPacket.id, GraphQLPacketType.Pong)
                    val frameOutPacketString = GraphktKtorJson.encodeToString(frameOutPacket)

                    send(frameOutPacketString)
                }

                GraphQLPacketType.Subscribe -> {
                    val subscriptionId = frameInPacket.id ?: run {
                        close(Close_InvalidMessage_PacketIdRequired)
                        return@webSocket
                    }

                    if (subscriptionId in sessionSubscriptions) {
                        close(Close_SubscriberAlreadyExists)
                        return@webSocket
                    }

                    if (!sessionInitialized.get()) {
                        close(Close_Unauthorized)
                    }

                    val subscriptionRequestObject = frameInPacket.payload ?: run {
                        close(Close_InvalidMessage_PacketPayloadRequired)
                        return@webSocket
                    }

                    val subscriptionRequest =
                        GraphktKtorJson.decodeFromJsonElement<GraphQLRequest>(subscriptionRequestObject)

                    val subscriptionResponseFlow = handler(subscriptionRequest)

                    val subscriptionJob = launch {
                        subscriptionResponseFlow.collect { subscriptionResponse ->
                            val subscriptionResponseObject =
                                GraphktKtorJson.encodeToJsonElement(subscriptionResponse).jsonObject

                            val frameOutPacket =
                                GraphQLPacket(subscriptionId, GraphQLPacketType.Next, subscriptionResponseObject)
                            val frameOutPacketString = GraphktKtorJson.encodeToString(frameOutPacket)

                            send(frameOutPacketString)
                        }
                        subscriptionResponseFlow.onCompletion {
                            val frameOutPacket = GraphQLPacket(subscriptionId, GraphQLPacketType.Complete)
                            val frameOutPacketString = GraphktKtorJson.encodeToString(frameOutPacket)

                            send(frameOutPacketString)
                        }
                    }

                    sessionSubscriptions[subscriptionId] = subscriptionJob
                }

                GraphQLPacketType.Complete -> {
                    val subscriptionId = frameInPacket.id ?: run {
                        close(Close_InvalidMessage_PacketIdRequired)
                        return@webSocket
                    }

                    sessionSubscriptions[subscriptionId]?.cancel()
                }

                GraphQLLegacyPacketType.Start -> {
                    val subscriptionId = frameInPacket.id ?: run {
                        close(Close_InvalidMessage_PacketIdRequired)
                        return@webSocket
                    }

                    if (subscriptionId in sessionLegacySubscriptions) {
                        close(Close_SubscriberAlreadyExists)
                        return@webSocket
                    }

                    if (!sessionInitialized.get()) {
                        close(Close_Unauthorized)
                    }

                    val subscriptionRequestObject = frameInPacket.payload ?: run {
                        close(Close_InvalidMessage_PacketPayloadRequired)
                        return@webSocket
                    }

                    val subscriptionRequest =
                        GraphktKtorJson.decodeFromJsonElement<GraphQLRequest>(subscriptionRequestObject)

                    val subscriptionResponseFlow = handler(subscriptionRequest)

                    val subscriptionJob = launch {
                        subscriptionResponseFlow.collect { subscriptionResponse ->
                            val subscriptionResponseObject =
                                GraphktKtorJson.encodeToJsonElement(subscriptionResponse).jsonObject

                            val frameOutPacket =
                                GraphQLPacket(subscriptionId, GraphQLLegacyPacketType.Data, subscriptionResponseObject)
                            val frameOutPacketString = GraphktKtorJson.encodeToString(frameOutPacket)

                            send(frameOutPacketString)
                        }
                        subscriptionResponseFlow.onCompletion {
                            val frameOutPacket = GraphQLPacket(subscriptionId, GraphQLPacketType.Complete)
                            val frameOutPacketString = GraphktKtorJson.encodeToString(frameOutPacket)

                            send(frameOutPacketString)
                        }
                    }

                    sessionLegacySubscriptions[subscriptionId] = subscriptionJob
                }

                GraphQLLegacyPacketType.Stop -> {
                    val subscriptionId = frameInPacket.id ?: run {
                        close(Close_InvalidMessage_PacketIdRequired)
                        return@webSocket
                    }

                    sessionLegacySubscriptions[subscriptionId]?.cancel()

                    close(Close_Normal)
                }

                else -> {
                    close(Close_InvalidMessage_PacketTypeUnexpected)
                }
            }
        }
    }
}
