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
import io.ktor.util.pipeline.*
import org.cufy.graphkt.AdvancedGraphktApi
import org.cufy.graphkt.GraphktEngineFactory
import org.cufy.graphkt.WithEngine
import org.cufy.graphkt.schema.*
import kotlin.time.Duration

/**
 * A scope holding the commonly available data
 * from a graphql implementation on every request.
 *
 * @author LSafer
 * @since 2.0.0
 */
class ConfigurationScope(
    /**
     * The execution context.
     */
    val context: MutableMap<Any?, Any?>,
    /**
     * The execution local.
     */
    val local: MutableMap<Any?, Any?>,
    /**
     * The application call instance.
     *
     * @since 2.0.0
     */
    val call: ApplicationCall
)

/**
 * Configuration of graphql ktor route.
 *
 * @author LSafer
 * @since 2.0.0
 */
class Configuration(
    val websocket: Boolean,
    val connectionInitWaitTimeout: Duration?,
    val engine: GraphktEngineFactory,
    val schemaBlock: GraphQLSchemaBuilderBlock,
    val beforeBlock: suspend ConfigurationScope.() -> Unit,
    val afterBlock: suspend ConfigurationScope.() -> Unit,
    val requestBlock: suspend ConfigurationScope.(GraphQLRequest) -> GraphQLRequest,
    val responseBlock: suspend ConfigurationScope.(GraphQLResponse) -> GraphQLResponse,
)

/**
 * The configuration for creating a graphql ktor route.
 *
 * @author LSafer
 * @since 2.0.0
 */
class ConfigurationBuilder : WithEngine, WithDeferredBuilder {
    override var engine: GraphktEngineFactory? = null

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

    /* blocks */

    /**
     * Code to be executed to configure the schema.
     */
    @AdvancedGraphktApi("Use `schema()` instead")
    val schemaBlocks: MutableList<GraphQLSchemaBuilderBlock> = mutableListOf()

    /**
     * Code to be executed before execution.
     */
    @AdvancedGraphktApi("Use `before()` instead")
    val beforeBlocks: MutableList<suspend ConfigurationScope.() -> Unit> = mutableListOf()

    /**
     * Code to be executed after execution (but before awaiting results).
     */
    @AdvancedGraphktApi("Use `after()` instead")
    val afterBlocks: MutableList<suspend ConfigurationScope.() -> Unit> = mutableListOf()

    /**
     * Code to be executed to transform request.
     */
    @AdvancedGraphktApi("Use `transformRequest()` instead")
    val requestBlocks: MutableList<suspend ConfigurationScope.(GraphQLRequest) -> GraphQLRequest> = mutableListOf()

    /**
     * Code to be executed to transform response.
     */
    @AdvancedGraphktApi("Use `transformResponse()` instead")
    val responseBlocks: MutableList<suspend ConfigurationScope.(GraphQLResponse) -> GraphQLResponse> = mutableListOf()

    @AdvancedGraphktApi("Use `deferred()` instead")
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    /**
     * Build the configuration.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    @OptIn(AdvancedGraphktApi::class)
    fun build(): Configuration {
        deferred.forEach { it() }
        deferred.clear()
        return Configuration(
            websocket = websocket,
            connectionInitWaitTimeout = connectionInitWaitTimeout,
            engine = engine
                ?: error("Graphkt Engine was not specified."),
            schemaBlock = schemaBlocks.toList().let {
                { it.forEach { it() } }
            },
            beforeBlock = beforeBlocks.toList().let {
                { it.forEach { it() } }
            },
            afterBlock = afterBlocks.toList().let {
                { it.forEach { it() } }
            },
            requestBlock = requestBlocks.toList().let {
                { req -> it.fold(req) { r, t -> t(r) } }
            },
            responseBlock = responseBlocks.toList().let {
                { res -> it.fold(res) { r, t -> t(r) } }
            }
        )
    }
}

/**
 * Construct a new [Configuration] with the given
 * builder [block].
 *
 * @param block the builder block.
 * @since 2.0.0
 */
fun Configuration(
    block: ConfigurationBuilder.() -> Unit = {}
): Configuration {
    val builder = ConfigurationBuilder()
    builder.apply(block)
    return builder.build()
}

/**
 * Disable the websocket implementation.
 */
fun ConfigurationBuilder.disableWebsocket() {
    websocket = false
}

/**
 * Configure the schema with the given [block].
 */
@OptIn(AdvancedGraphktApi::class)
fun ConfigurationBuilder.schema(
    block: GraphQLSchemaBuilderBlock
) {
    schemaBlocks += block
}

/**
 * Add the given [block] to configure execution context.
 */
@Deprecated("context block was replaced with before block", ReplaceWith("before { context.apply(block) }"))
fun ConfigurationBuilder.context(
    block: suspend ConfigurationScope.(MutableMap<Any?, Any?>) -> Unit
) {
    before { block(context) }
}

/**
 * Add the given [block] to configure initial local.
 */
@Deprecated("local block was replaced with before block", ReplaceWith("before { local.apply(block) }"))
fun ConfigurationBuilder.local(
    block: suspend ConfigurationScope.(MutableMap<Any?, Any?>) -> Unit
) {
    before { block(local) }
}

/**
 * Add the given [block] to be invoked before execution.
 */
@OptIn(AdvancedGraphktApi::class)
fun ConfigurationBuilder.before(
    block: suspend ConfigurationScope.() -> Unit
) {
    beforeBlocks += block
}

/**
 * Add the given [block] to be invoked after execution. (but before awaiting results)
 */
@OptIn(AdvancedGraphktApi::class)
fun ConfigurationBuilder.after(
    block: suspend ConfigurationScope.() -> Unit
) {
    afterBlocks += block
}

/**
 * Add the given [block] to transform the request
 * before the execution.
 */
@OptIn(AdvancedGraphktApi::class)
fun ConfigurationBuilder.transformRequest(
    block: suspend ConfigurationScope.(GraphQLRequest) -> GraphQLRequest
) {
    requestBlocks += block
}

/**
 * Add the given [block] to transform the response
 * before sending it to the client.
 */
@OptIn(AdvancedGraphktApi::class)
fun ConfigurationBuilder.transformResponse(
    block: suspend ConfigurationScope.(GraphQLResponse) -> GraphQLResponse
) {
    responseBlocks += block
}

/**
 * Add the given [block] to transform the response
 * errors before sending it to the client.
 */
fun ConfigurationBuilder.transformError(
    block: suspend ConfigurationScope.(GraphQLError) -> GraphQLError
) {
    transformResponse {
        it.copy(errors = it.errors?.map { block(it) })
    }
}
