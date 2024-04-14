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

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.cufy.graphkt.ExperimentalGraphktApi
import org.cufy.graphkt.ktor.internal.graphqlHttp
import org.cufy.graphkt.ktor.internal.graphqlWebsocket
import org.cufy.graphkt.schema.*

/**
 * Handle graphql requests to the given [path].
 *
 * @param path the route path.
 * @param block configuration block.
 * @since 2.0.0
 */
@KtorDsl
fun Application.graphql(
    path: String = "/graphql",
    block: GraphQLKtorMutableConfiguration.() -> Unit = {}
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
 * @since 2.0.0
 */
@OptIn(ExperimentalGraphktApi::class)
@KtorDsl
fun Route.graphql(
    path: String = "/graphql",
    block: GraphQLKtorMutableConfiguration.() -> Unit = {}
) {
    val configuration = GraphQLKtorConfiguration(block)

    /* prepare base arguments */

    val schema = GraphQLSchema {
        if (configuration.builtins) {
            additionalType(GraphQLLongType)
            additionalType(GraphQLDecimalType)
            additionalType(GraphQLIntegerType)
            additionalType(GraphQLVoidType)
        }

        configuration.schemaBlocks.forEach { it(this) }
    }

    val engine = configuration.engine(schema)

    suspend fun handleRequest(
        request: GraphQLRequest,
        call: ApplicationCall
    ): Flow<GraphQLResponse> {
        /* prepare execution arguments */

        val context = mutableMapOf<Any?, Any?>()
        val local = mutableMapOf<Any?, Any?>()

        // making `call` extension available in getter scope
        context["call"] = call

        val scope = GraphQLKtorExecutionScope(context, local, call)

        configuration.beforeBlocks.forEach { it(scope) }

        val transformedRequest = configuration.requestTransformers
            .fold(request) { it, block -> block(scope, it) }

        // actual execution
        val responseFlow = engine.execute(transformedRequest, context, local)

        configuration.afterBlocks.forEach { it(scope) }

        return responseFlow.map { response ->
            val transformedResponse = configuration.responseTransformers
                .fold(response) { it, block -> block(scope, it) }

            transformedResponse
        }
    }

    // add the routes (the listeners)

    graphqlHttp(path) { handleRequest(it, call) }

    if (configuration.websocket) {
        graphqlWebsocket(path, configuration) { handleRequest(it, call) }
    }

    val graphqls = configuration.graphqls

    if (graphqls != null) {
        get(graphqls) {
            call.respondTextWriter(ContentType.Text.Plain) {
                val input = withContext(Dispatchers.IO) {
                    engine.obtainSchemaReader()
                }

                input.use { it.copyTo(this) }
            }
        }
    }
}
