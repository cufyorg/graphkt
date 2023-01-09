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

import kotlinx.serialization.SerialName
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
enum class GraphQLPacketType {
    @SerialName("connection_init")
    ConnectionInit,

    @SerialName("connection_ack")
    ConnectionAck,

    @SerialName("ping")
    Ping,

    @SerialName("pong")
    Pong,

    @SerialName("subscribe")
    Subscribe,

    @SerialName("next")
    Next
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
    val type: GraphQLPacketType,
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
