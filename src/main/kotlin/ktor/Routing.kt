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
package org.cufy.kaguya.ktor

import graphql.ExecutionInput
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.future.await
import kotlinx.serialization.json.Json
import org.cufy.kaguya.GraphQL
import org.cufy.kaguya.GraphQLContext
import org.cufy.kaguya.GraphQLSchema
import org.cufy.kaguya.Kaguya
import org.cufy.kaguya.internal.dynamicEncodeToString

/**
 * Builds a route to match graphql `POST` requests
 * and respond to them with the result of executing
 * a graphql schema built with the given [block].
 *
 * @since 1.0.0
 */
@KtorDsl
fun Application.graphql(
    path: String = "/graphql",
    block: Configuration.() -> Unit = {}
) {
    routing {
        graphql(path, block)
    }
}

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql playground
 * html.
 */
@KtorDsl
fun Application.graphiql(
    path: String = "/graphql"
) {
    routing {
        graphiql(path)
    }
}

/**
 * Builds a route to match graphql `POST` requests
 * and respond to them with the result of executing
 * a graphql schema built with the given [block].
 *
 * @since 1.0.0
 */
@KtorDsl
fun Route.graphql(
    path: String = "",
    block: Configuration.() -> Unit = {}
) {
    val configuration = Configuration()
    configuration.apply(block)
    configuration.configurationBlock(configuration)
    val schema = GraphQLSchema(configuration.schemaBlock)
    val graphql = GraphQL(schema, configuration.graphqlBlock)

    if (configuration.graphiql)
        graphiql(path)

    post(path) {
        val request = call.receive<GraphQLRequest>()

        val context =
            GraphQLContext {
                put("call", call)
                put("pipeline", this@post)
                configuration.contextBlock(this, this@post)
            }

        val executionInput =
            ExecutionInput.newExecutionInput()
                .operationName(request.operationName)
                .query(request.query)
                .variables(request.variables ?: mapOf())
                .graphQLContext { it.of(context) }
                .also { configuration.executionInputBlock(it, this) }
                .build()

        val executionResult =
            graphql.executeAsync(executionInput).await()

        val result = Json.dynamicEncodeToString(
            executionResult.toSpecification()
        )

        call.respondText(result, ContentType.Application.Json)
    }
}

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql playground
 * html.
 */
@KtorDsl
fun Route.graphiql(
    path: String = ""
) {
    get(path) {
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val result = Kaguya::class.java.classLoader
            .getResource("graphiql.html")
            .readBytes()

        call.respondBytes(result, ContentType.Text.Html)
    }
}
