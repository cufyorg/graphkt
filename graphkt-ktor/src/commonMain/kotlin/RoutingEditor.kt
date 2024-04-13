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
import kotlinx.coroutines.withContext

// Playground

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql playground
 * html.
 */
@KtorDsl
fun Application.playground(
    path: String = "/graphql"
) {
    routing {
        playground(path)
    }
}

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql playground
 * html.
 */
@KtorDsl
fun Route.playground(
    path: String = "/graphql"
) {
    val classloader = GraphQLKtorConfiguration::class.java.classLoader
    val resource = classloader.getResource("playground.html")!!

    get(path) {
        call.respondOutputStream(ContentType.Text.Html) {
            val input = withContext(Dispatchers.IO) {
                resource.openStream()
            }

            input.use { it.copyTo(this) }
        }
    }
}

// Sandbox

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql sandbox
 * html.
 */
@KtorDsl
fun Application.sandbox(
    path: String = "/graphql"
) {
    routing {
        sandbox(path)
    }
}

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql sandbox
 * html.
 */
@KtorDsl
fun Route.sandbox(
    path: String = "/graphql"
) {
    val classloader = GraphQLKtorConfiguration::class.java.classLoader
    val resource = classloader.getResource("sandbox.html")!!

    get(path) {
        call.respondOutputStream(ContentType.Text.Html) {
            val input = withContext(Dispatchers.IO) {
                resource.openStream()
            }

            input.use { it.copyTo(this) }
        }
    }
}

// graphiql

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphiql html.
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
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphiql html.
 */
@KtorDsl
fun Route.graphiql(
    path: String = "/graphql"
) {
    val classloader = GraphQLKtorConfiguration::class.java.classLoader
    val resource = classloader.getResource("graphiql.html")!!

    get(path) {
        call.respondOutputStream(ContentType.Text.Html) {
            val input = withContext(Dispatchers.IO) {
                resource.openStream()
            }

            input.use { it.copyTo(this) }
        }
    }
}
