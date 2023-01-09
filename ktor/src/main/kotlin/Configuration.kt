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
package org.cufy.graphkt.ktor

import io.ktor.server.application.*
import org.cufy.graphkt.AdvancedGraphktApi
import org.cufy.graphkt.GraphktEngineFactory
import org.cufy.graphkt.schema.*
import kotlin.time.Duration

/**
 * The configuration for creating a graphql ktor route.
 *
 * @param TConfiguration the engine configuration type.
 * @author LSafer
 * @since 2.0.0
 */
class Configuration<TConfiguration> : WithDeferredBuilder {
    /**
     * True, to enable `graphql-ws` implementation.
     *
     * @since 2.0.0
     */
    var websocket: Boolean = true

    /**
     * Websocket connection initialization timeout.
     *
     * @since 2.0.0
     */
    var connectionInitWaitTimeout: Duration? = null

    /**
     * The engine factory.
     */
    @AdvancedGraphktApi("Use `engine()` instead")
    var engineFactory: GraphktEngineFactory<TConfiguration>? = null

    /* blocks */

    /**
     * Code to be executed to configure the engine.
     */
    @AdvancedGraphktApi("Use `engine()` instead")
    val engineBlock: MutableList<TConfiguration.() -> Unit> = mutableListOf()

    /**
     * Code to be executed to configure the schema.
     */
    @AdvancedGraphktApi("Use `schema()` instead")
    val schemaBlock: MutableList<GraphQLSchemaBuilderBlock> = mutableListOf()

    /**
     * Code to be executed to configure execution context.
     */
    @AdvancedGraphktApi("Use `context()` instead")
    val contextBlock: MutableList<MutableMap<Any?, Any?>.(ApplicationCall) -> Unit> = mutableListOf()

    /**
     * Code to be executed to configure initial execution local.
     */
    @AdvancedGraphktApi("Use `local()` instead")
    val localBlock: MutableList<MutableMap<Any?, Any?>.(ApplicationCall) -> Unit> = mutableListOf()

    /**
     * Code to be executed to transform request.
     */
    @AdvancedGraphktApi("Use `transformRequest()` instead")
    val requestBlock: MutableList<(GraphQLRequest) -> GraphQLRequest> = mutableListOf()

    /**
     * Code to be executed to transform response.
     */
    @AdvancedGraphktApi("Use `transformResponse()` instead")
    val responseBlock: MutableList<(GraphQLResponse) -> GraphQLResponse> = mutableListOf()

    @AdvancedGraphktApi("Use `deferred()` instead")
    override val deferred: MutableList<() -> Unit> = mutableListOf()
}

/**
 * Disable the websocket implementation.
 */
fun <TConfiguration> Configuration<TConfiguration>.disableWebsocket() {
    websocket = false
}

/**
 * Set the engine factory.
 *
 * @param factory the engine factory.
 * @param block the engine configuration.
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun <TConfiguration> Configuration<TConfiguration>.engine(
    factory: GraphktEngineFactory<TConfiguration>,
    block: TConfiguration.() -> Unit = {}
) {
    engineFactory = factory
    engineBlock += block
}

/**
 * Configure the engine with the given [block].
 */
@OptIn(AdvancedGraphktApi::class)
fun <TConfiguration> Configuration<TConfiguration>.engine(
    block: TConfiguration.() -> Unit
) {
    engineBlock += block
}

/**
 * Configure the schema with the given [block].
 */
@OptIn(AdvancedGraphktApi::class)
fun <TConfiguration> Configuration<TConfiguration>.schema(
    block: GraphQLSchemaBuilderBlock
) {
    schemaBlock += block
}

/**
 * Add the given [block] to configure execution context.
 */
@OptIn(AdvancedGraphktApi::class)
fun <TConfiguration> Configuration<TConfiguration>.context(
    block: MutableMap<Any?, Any?>.(ApplicationCall) -> Unit
) {
    contextBlock += block
}

/**
 * Add the given [block] to configure initial local.
 */
@OptIn(AdvancedGraphktApi::class)
fun <TConfiguration> Configuration<TConfiguration>.local(
    block: MutableMap<Any?, Any?>.(ApplicationCall) -> Unit
) {
    localBlock += block
}

/**
 * Add the given [block] to transform the request
 * before the execution.
 */
@OptIn(AdvancedGraphktApi::class)
fun <TConfiguration> Configuration<TConfiguration>.transformRequest(
    block: (GraphQLRequest) -> GraphQLRequest
) {
    requestBlock += block
}

/**
 * Add the given [block] to transform the response
 * before sending it to the client.
 */
@OptIn(AdvancedGraphktApi::class)
fun <TConfiguration> Configuration<TConfiguration>.transformResponse(
    block: (GraphQLResponse) -> GraphQLResponse
) {
    responseBlock += block
}

/**
 * Add the given [block] to transform the response
 * errors before sending it to the client.
 */
fun <TConfiguration> Configuration<TConfiguration>.transformError(
    block: (GraphQLError) -> GraphQLError
) {
    transformResponse {
        it.copy(errors = it.errors?.map(block))
    }
}