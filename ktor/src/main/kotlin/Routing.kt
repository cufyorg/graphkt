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
 * @since 2.0.0
 */
@KtorDsl
fun Application.graphql(
    path: String = "/graphql",
    block: ConfigurationBuilder.() -> Unit = {}
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
@KtorDsl
@OptIn(InternalGraphktApi::class)
fun Route.graphql(
    path: String = "/graphql",
    block: ConfigurationBuilder.() -> Unit = {}
) {
    val configuration = Configuration(block)

    /* prepare base arguments */

    val schema = GraphQLSchema {
        configuration.schemaBlock(this)
    }

    val engine = configuration.engine(schema)

    suspend fun handleRequest(
        request: GraphQLRequest,
        call: ApplicationCall
    ): Flow<GraphQLResponse> {
        /* prepare execution arguments */

        val context = mutableMapOf<Any?, Any?>()
        val local = mutableMapOf<Any?, Any?>()

        context["call"] = call

        val scope = ConfigurationScope(context, local, call)

        configuration.beforeBlock(scope)

        val transformedRequest =
            configuration.requestBlock(scope, request)

        // actual execution
        val responseFlow = engine.execute(transformedRequest, context, local)

        configuration.afterBlock(scope)

        return responseFlow.map { response ->
            val transformedResponse =
                configuration.responseBlock(scope, response)

            transformedResponse
        }
    }

    // add the routes (the listeners)

    graphqlHttp(path) { handleRequest(it, call) }

    if (configuration.websocket) {
        graphqlWebsocket(path, configuration) { handleRequest(it, call) }
    }
}
