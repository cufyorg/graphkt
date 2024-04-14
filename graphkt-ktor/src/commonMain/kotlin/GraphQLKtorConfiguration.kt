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
import org.cufy.graphkt.ExperimentalGraphktApi
import org.cufy.graphkt.GraphQLElementWithEngine
import org.cufy.graphkt.GraphQLEngineFactory
import org.cufy.graphkt.GraphQLMutableElementWithEngine
import org.cufy.graphkt.schema.GraphQLError
import org.cufy.graphkt.schema.GraphQLRequest
import org.cufy.graphkt.schema.GraphQLResponse
import org.cufy.graphkt.schema.GraphQLSchemaBlock
import kotlin.time.Duration

/* ============================================== */
/* ========|                            |======== */
/* ========| Execution Scope            |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A scope holding the commonly available data
 * from a graphql implementation on every request.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLKtorExecutionScope(
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

typealias GraphQLKtorExecutionBlock =
        suspend GraphQLKtorExecutionScope.() -> Unit

typealias GraphQLKtorRequestTransformer =
        suspend GraphQLKtorExecutionScope.(GraphQLRequest) -> GraphQLRequest

typealias GraphQLKtorResponseTransformer =
        suspend GraphQLKtorExecutionScope.(GraphQLResponse) -> GraphQLResponse

/* ============================================== */
/* ========|                            |======== */
/* ========| Ktor Configuration         |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * Configuration of graphql ktor route.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLKtorConfiguration :
    GraphQLElementWithEngine {
    /**
     * True, to enable `http` implementation.
     *
     * @since 2.0.0
     */
    val http: Boolean /* = true */

    /**
     * True, to enable `graphql-ws` implementation.
     *
     * @since 2.0.0
     */
    val websocket: Boolean /* = true */

    /**
     * True, to add all the non-standard builtin scalars as additional types.
     *
     * Standard built-in scalars are added anyway.
     *
     * @since 2.0.0
     */
    val builtins: Boolean /* = true */

    /**
     * The path of the endpoint to print the schema to.
     */
    val graphqls: String? /* = null */

    /**
     * Websocket connection initialization timeout.
     *
     * @since 2.0.0
     */
    val connectionInitWaitTimeout: Duration? /* = null */

    /**
     * Code to be executed to configure the schema.
     */
    val schemaBlocks: List<GraphQLSchemaBlock> /* = emptyList() */

    /**
     * Code to be executed before execution.
     */
    val beforeBlocks: List<GraphQLKtorExecutionBlock> /* = emptyList() */

    /**
     * Code to be executed after execution (but before awaiting results).
     */
    val afterBlocks: List<GraphQLKtorExecutionBlock> /* = emptyList() */

    /**
     * Code to be executed to transform request.
     */
    val requestTransformers: List<GraphQLKtorRequestTransformer> /* = emptyList() */

    /**
     * Code to be executed to transform response.
     */
    val responseTransformers: List<GraphQLKtorResponseTransformer> /* = emptyList() */
}

/**
 * The configuration for creating a graphql ktor route.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLKtorMutableConfiguration :
    GraphQLMutableElementWithEngine,
    GraphQLKtorConfiguration {
    override var http: Boolean /* = true */
    override var websocket: Boolean /* = true */
    override var builtins: Boolean /* = true */
    override var graphqls: String? /* = null */
    override var connectionInitWaitTimeout: Duration? /* = null */
    override val schemaBlocks: MutableList<GraphQLSchemaBlock> /* = mutableListOf() */
    override val beforeBlocks: MutableList<GraphQLKtorExecutionBlock> /* = mutableListOf() */
    override val afterBlocks: MutableList<GraphQLKtorExecutionBlock> /* = mutableListOf() */
    override val requestTransformers: MutableList<GraphQLKtorRequestTransformer> /* = mutableListOf() */
    override val responseTransformers: MutableList<GraphQLKtorResponseTransformer> /* = mutableListOf() */
}

/**
 * Construct a new [GraphQLKtorConfiguration] with the given arguments.
 */
@OptIn(ExperimentalGraphktApi::class)
fun GraphQLKtorConfiguration(
    engine: GraphQLEngineFactory,
    http: Boolean = true,
    websocket: Boolean = true,
    builtins: Boolean = true,
    graphqls: String? = null,
    connectionInitWaitTimeout: Duration? = null,
    schemaBlocks: List<GraphQLSchemaBlock> = emptyList(),
    beforeBlocks: List<GraphQLKtorExecutionBlock> = emptyList(),
    afterBlocks: List<GraphQLKtorExecutionBlock> = emptyList(),
    requestTransformers: List<GraphQLKtorRequestTransformer> = emptyList(),
    responseTransformers: List<GraphQLKtorResponseTransformer> = emptyList(),
): GraphQLKtorConfiguration {
    return object : GraphQLKtorConfiguration {
        override val engine = engine
        override val http = http
        override val websocket = websocket
        override val builtins = builtins
        override val graphqls = graphqls
        override val connectionInitWaitTimeout = connectionInitWaitTimeout
        override val schemaBlocks = schemaBlocks
        override val beforeBlocks = beforeBlocks
        override val afterBlocks = afterBlocks
        override val requestTransformers = requestTransformers
        override val responseTransformers = responseTransformers
    }
}

/**
 * Construct a new [GraphQLKtorMutableConfiguration].
 */
@OptIn(ExperimentalGraphktApi::class)
fun GraphQLKtorMutableConfiguration(): GraphQLKtorMutableConfiguration {
    return object : GraphQLKtorMutableConfiguration {
        override lateinit var engine: GraphQLEngineFactory
        override var http: Boolean = true
        override var websocket: Boolean = true
        override var builtins: Boolean = true
        override var graphqls: String? = null
        override var connectionInitWaitTimeout: Duration? = null
        override val schemaBlocks = mutableListOf<GraphQLSchemaBlock>()
        override val beforeBlocks = mutableListOf<GraphQLKtorExecutionBlock>()
        override val afterBlocks = mutableListOf<GraphQLKtorExecutionBlock>()
        override val requestTransformers = mutableListOf<GraphQLKtorRequestTransformer>()
        override val responseTransformers = mutableListOf<GraphQLKtorResponseTransformer>()
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
@OptIn(ExperimentalGraphktApi::class)
fun GraphQLKtorConfiguration.copy(
    engine: GraphQLEngineFactory = this.engine,
    websocket: Boolean = this.websocket,
    builtins: Boolean = this.builtins,
    graphqls: String? = this.graphqls,
    connectionInitWaitTimeout: Duration? = this.connectionInitWaitTimeout,
    schemaBlocks: List<GraphQLSchemaBlock> = this.schemaBlocks,
    beforeBlocks: List<GraphQLKtorExecutionBlock> = this.beforeBlocks,
    afterBlocks: List<GraphQLKtorExecutionBlock> = this.afterBlocks,
    requestTransformers: List<GraphQLKtorRequestTransformer> = this.requestTransformers,
    responseTransformers: List<GraphQLKtorResponseTransformer> = this.responseTransformers
): GraphQLKtorConfiguration {
    return GraphQLKtorConfiguration(
        engine = engine,
        websocket = websocket,
        builtins = builtins,
        graphqls = graphqls,
        connectionInitWaitTimeout = connectionInitWaitTimeout,
        schemaBlocks = schemaBlocks,
        beforeBlocks = beforeBlocks,
        afterBlocks = afterBlocks,
        requestTransformers = requestTransformers,
        responseTransformers = responseTransformers
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLKtorMutableConfiguration].
 */
typealias GraphQLKtorConfigurationBlock =
        GraphQLKtorMutableConfiguration.() -> Unit

/**
 * Construct a new [GraphQLKtorConfiguration] with the given
 * builder [block].
 *
 * @param block the builder block.
 * @since 2.0.0
 */
@OptIn(ExperimentalGraphktApi::class)
fun GraphQLKtorConfiguration(
    block: GraphQLKtorConfigurationBlock = {}
): GraphQLKtorConfiguration {
    val builder = GraphQLKtorMutableConfiguration()
    builder.apply(block)
    return builder.copy()
}

/* ---------------------------------------------- */

/**
 * Disable the websocket implementation.
 */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("replace with `websocket = false`")
fun GraphQLKtorMutableConfiguration.disableWebsocket() {
    websocket = false
}

/**
 * Add an endpoint to obtain the schema from at `/graphqls`
 */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("replace with `graphqls = \"graphqls\"`")
fun GraphQLKtorMutableConfiguration.enableGraphQLS() {
    graphqls = "/graphqls"
}

/**
 * Configure the schema with the given [block].
 */
fun GraphQLKtorMutableConfiguration.schema(
    block: GraphQLSchemaBlock
) {
    schemaBlocks += block
}

/**
 * Add the given [block] to configure execution context.
 */
@Deprecated("context block was replaced with before block", ReplaceWith("before { context.apply(block) }"))
fun GraphQLKtorMutableConfiguration.context(
    block: suspend GraphQLKtorExecutionScope.(MutableMap<Any?, Any?>) -> Unit
) {
    before { block(context) }
}

/**
 * Add the given [block] to configure initial local.
 */
@Deprecated("local block was replaced with before block", ReplaceWith("before { local.apply(block) }"))
fun GraphQLKtorMutableConfiguration.local(
    block: suspend GraphQLKtorExecutionScope.(MutableMap<Any?, Any?>) -> Unit
) {
    before { block(local) }
}

/**
 * Add the given [block] to be invoked before execution.
 */
fun GraphQLKtorMutableConfiguration.before(
    block: suspend GraphQLKtorExecutionScope.() -> Unit
) {
    beforeBlocks += block
}

/**
 * Add the given [block] to be invoked after execution. (but before awaiting results)
 */
fun GraphQLKtorMutableConfiguration.after(
    block: suspend GraphQLKtorExecutionScope.() -> Unit
) {
    afterBlocks += block
}

/**
 * Add the given [block] to transform the request
 * before the execution.
 */
fun GraphQLKtorMutableConfiguration.transformRequest(
    block: suspend GraphQLKtorExecutionScope.(GraphQLRequest) -> GraphQLRequest
) {
    requestTransformers += block
}

/**
 * Add the given [block] to transform the response
 * before sending it to the client.
 */
fun GraphQLKtorMutableConfiguration.transformResponse(
    block: suspend GraphQLKtorExecutionScope.(GraphQLResponse) -> GraphQLResponse
) {
    responseTransformers += block
}

/**
 * Add the given [block] to transform the response
 * errors before sending it to the client.
 */
fun GraphQLKtorMutableConfiguration.transformError(
    block: suspend GraphQLKtorExecutionScope.(GraphQLError) -> GraphQLError
) {
    transformResponse {
        it.copy(errors = it.errors?.map { error -> block(error) })
    }
}

/**
 * Add the given [block] to transform the response
 * errors before sending it to the client.
 */
inline fun <reified T : Throwable> GraphQLKtorMutableConfiguration.transformException(
    crossinline block: GraphQLKtorExecutionScope.(T, GraphQLError) -> GraphQLError
) {
    transformError {
        when (val cause = it.cause) {
            is T -> block(cause, it)
            else -> it
        }
    }
}

/* ============================================== */
