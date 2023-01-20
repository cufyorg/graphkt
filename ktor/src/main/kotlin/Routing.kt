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
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.cufy.graphkt.AdvancedGraphktApi
import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.ktor.internal.graphqlHttp
import org.cufy.graphkt.ktor.internal.graphqlWebsocket
import org.cufy.graphkt.schema.GraphQLRequest
import org.cufy.graphkt.schema.GraphQLResponse
import org.cufy.graphkt.schema.GraphQLSchema

/**
 * Handle graphql requests to the given [path].
 *
 * @param path the route path.
 * @param block configuration block.
 * @param TConfiguration the engine configuration type.
 * @since 2.0.0
 */
@KtorDsl
fun <TConfiguration> Application.graphql(
    path: String = "/graphql",
    block: Configuration<TConfiguration>.() -> Unit = {}
) {
    routing {
        graphql(path, block)
    }
}

/**
 * Handle graphql requests to the given [path].
 *
 * @param path the route path.
 * @param block configuration block.
 * @param TConfiguration the engine configuration type.
 * @since 2.0.0
 */
@KtorDsl
@OptIn(AdvancedGraphktApi::class, InternalGraphktApi::class)
fun <TConfiguration> Route.graphql(
    path: String = "/graphql",
    block: Configuration<TConfiguration>.() -> Unit = {}
) {
    val configuration = Configuration<TConfiguration>()
    configuration.apply(block)
    configuration.deferred.forEach { it() }
    configuration.deferred.clear()

    //

    val websocket = configuration.websocket

    val engineFactory = configuration.engineFactory
        ?: error("Graphkt Engine was not specified.")

    val engineBlock = configuration.engineBlock
    val schemaBlock = configuration.schemaBlock
    val requestBlock = configuration.requestBlock.toList()
    val responseBlock = configuration.responseBlock.toList()
    val contextBlock = configuration.contextBlock.toList()
    val localBlock = configuration.localBlock.toList()
    val connectionInitWaitTimeout = configuration.connectionInitWaitTimeout

    /* prepare base arguments */

    val schema = GraphQLSchema {
        schemaBlock.forEach { it() }
    }

    val engine = engineFactory(schema) {
        engineBlock.forEach { it() }
    }

    suspend fun handleRequest(
        request: GraphQLRequest,
        call: ApplicationCall
    ): Flow<GraphQLResponse> {
        val scope = ConfigurationScope(call)

        var req = request

        // apply request block
        requestBlock.forEach { req = it(scope, req) }

        /* prepare execution arguments */

        val context = buildMap {
            put("call", call)
            contextBlock.forEach { it(scope, this@buildMap) }
        }

        val local = buildMap {
            localBlock.forEach { it(scope, this@buildMap) }
        }

        // actual execution
        val resFlow = engine.execute(req, context, local)

        return resFlow.map { response ->
            var res = response

            // apply response block
            responseBlock.forEach { res = it(scope, res) }

            res
        }
    }

    // add the routes (the listeners)

    graphqlHttp(path) { handleRequest(it, call) }

    if (websocket) {
        graphqlWebsocket(path, connectionInitWaitTimeout) { handleRequest(it, call) }
    }
}
