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
package org.cufy.graphkt.schema

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

/**
 * The supported packet types.
 *
 * @author LSafer
 * @since 2.0.0
 */
@Serializable
object GraphQLPacketType {
    /**
     * #### Client -> Server
     *
     * Indicates that the client wants to
     * establish a connection within the existing
     * socket. This connection is not the actual
     * WebSocket communication channel, but is
     * rather a frame within it asking the server
     * to allow future operation requests.
     *
     * The server must receive the connection
     * initialisation message within the allowed
     * waiting time specified in the
     * `connectionInitWaitTimeout` parameter
     * during the server setup. If the client does
     * not request a connection within the allowed
     * timeout, the server will close the socket
     * with the event:
     * `4408: Connection initialisation timeout`.
     *
     * If the server receives more than one
     * [ConnectionInit] message at any given time,
     * the server will close the socket with the
     * event:
     * `4429: Too many initialisation requests`.
     *
     * The `payload` is optional and can be used
     * for anything.
     *
     * @since 2.0.0
     */
    const val ConnectionInit = "connection_init"

    /**
     * #### Server -> Client
     *
     * Expected response to the ConnectionInit
     * message from the client acknowledging a
     * successful connection with the server.
     *
     * The `payload` is optional and can be used
     * for anything.
     *
     * @since 2.0.0
     */
    const val ConnectionAck = "connection_ack"

    /**
     * #### Bidirectional
     *
     * Useful for detecting failed connections,
     * displaying latency metrics or other types
     * of network probing.
     *
     * A [Pong] must be sent in response from the
     * receiving party as soon as possible.
     *
     * The [Ping] message can be sent at any time
     * within the established socket.
     *
     * The `payload` is optional and can be used
     * for anything.
     *
     * @since 2.0.0
     */
    const val Ping = "ping"

    /**
     * #### Bidirectional
     *
     * The response to the [Ping] message. Must be
     * sent as soon as the Ping message is received.
     *
     * The [Pong] message can be sent at any time
     * within the established socket. Furthermore,
     * the [Pong] message may even be sent
     * unsolicited as a unidirectional heartbeat.
     *
     * The `payload` is optional and can be used
     * for anything.
     *
     * @since 2.0.0
     */
    const val Pong = "pong"

    /**
     * #### Client -> Server
     *
     * Requests an operation specified in the
     * message `payload`. This message provides a
     * unique ID field to connect published
     * messages to the operation requested by this
     * message.
     *
     * If there is already an active subscriber
     * for an operation matching the provided ID,
     * regardless of the operation type, the
     * server must close the socket immediately
     * with the event:
     * `4409: Subscriber for <unique-operation-id> already exists`.
     *
     * The server needs only keep track of IDs for
     * as long as the subscription is active. Once
     * a client completes an operation, it is free
     * to re-use that ID.
     *
     * Executing operations is allowed only after
     * the server has acknowledged the connection
     * through the [ConnectionAck] message, if the
     * connection is not acknowledged, the socket
     * will be closed immediately with the event:
     * `4401: Unauthorized`.
     *
     * The payload shall be of type [GraphQLRequest].
     *
     * @since 2.0.0
     */
    const val Subscribe = "subscribe"

    /**
     * #### Server -> Client
     *
     * Operation execution result(s) from the
     * source stream created by the binding
     * [Subscribe] message. After all results have
     * been emitted, the [Complete] message will
     * follow indicating stream completion.
     *
     * The payload shall be of type [GraphQLResponse]
     *
     * @since 2.0.0
     */
    const val Next = "next"

    /**
     * #### Server -> Client
     *
     * Operation execution error(s) in response to
     * the [Subscribe] message. This can occur
     * before execution starts, usually due to
     * validation errors, or during the execution
     * of the request. This message terminates the
     * operation and no further messages will be
     * sent.
     *
     * The payload shall be of type [GraphQLError].
     *
     * @since 2.0.0
     */
    const val Error = "error"

    /**
     * #### Server -> Client
     * Indicates that the requested operation
     * execution has completed. If the server
     * dispatched the [Error] message relative to
     * the original [Subscribe] message, no
     * [Complete] message will be emitted.
     *
     * #### Client -> Server
     * Indicates that the client has stopped
     * listening and wants to complete the
     * subscription. No further events, relevant
     * to the original subscription, should be
     * sent through. Even if the client sent a
     * [Complete] message for a single-result-
     * operation before it resolved, the result
     * should not be sent through once it does.
     *
     * ####
     *
     * Note: The asynchronous nature of the
     * full-duplex connection means that a client
     * can send a [Complete] message to the server
     * even when messages are in-flight to the
     * client, or when the server has itself
     * completed the operation (via a [Error] or
     * [Complete] message). Both client and server
     * must therefore be prepared to receive (and
     * ignore) messages for operations that they
     * consider already completed.
     */
    const val Complete = "complete"
}

/**
 * Legacy packet types.
 *
 * @author LSafer
 * @since 2.0.0
 */
@Serializable
object GraphQLLegacyPacketType {
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

/**
 * The packet of a graphql web socket frame.
 *
 * @author LSafer
 * @since 2.0.0
 */
@Serializable
data class GraphQLPacket(
    /**
     * The packet id.
     */
    val id: String? = null,
    /**
     * The packet type.
     */
    val type: String,
    /**
     * The packet's payload.
     */
    val payload: JsonObject? = buildJsonObject { }
)

/**
 * The body of a graphql request.
 *
 * @author LSafer
 * @since 2.0.0
 */
@Serializable
data class GraphQLRequest(
    /**
     * The full graphql query containing the operation type.
     *
     * _can be either query or mutation_
     *
     * @since 2.0.0
     */
    val query: String,
    /**
     * The operation name.
     *
     * _Optional, but if included, must be present in the query_
     *
     * @since 2.0.0
     */
    val operationName: String? = null,
    /**
     * The query variables.
     *
     * _Optional if there are no variables included in the query_
     *
     * @since 2.0.0
     */
    val variables: JsonObject? = null,
    /**
     * Verbose extensions.
     */
    val extensions: JsonObject? = null
)

/**
 * The body of a graphql response.
 *
 * @author LSafer
 * @since 2.0.0
 */
@Serializable
data class GraphQLResponse(
    /**
     * The execution errors, if any.
     *
     * @since 2.0.0
     */
    val errors: List<GraphQLError>?,
    /**
     * The execution result. If succeeded.
     *
     * @since 2.0.0
     */
    val data: JsonObject?,
    /**
     * Verbose extensions. (can be anything)
     *
     * @since 2.0.0
     */
    val extensions: JsonObject?
)

/**
 * The definition of a graphql error.
 *
 * @author LSafer
 * @since 2.0.0
 */
@Serializable
data class GraphQLError(
    /**
     * User-friendly error message.
     */
    val message: String,
    /**
     * A list of the locations associated with this error.
     *
     * @since 2.0.0
     */
    val locations: List<GraphQLErrorLocation>?,
    /**
     * Details the path of the response field which experienced the error.
     *
     * @since 2.0.0
     */
    val path: List<JsonPrimitive>?,
    /**
     * Verbose extensions. (can be anything)
     *
     * @since 2.0.0
     */
    val extensions: JsonObject?,
    /**
     * The cause of the error.
     */
    @Transient
    val cause: Throwable? = null
)

/**
 * The definition of a location associated with
 * some error.
 *
 * @author LSafer
 * @since 2.0.0
 */
@Serializable
data class GraphQLErrorLocation(
    /**
     * The line number. `>0`
     *
     * @since 2.0.0
     */
    val line: Int,
    /**
     * The column number. `>0`
     *
     * @since 2.0.0
     */
    val column: Int
)
