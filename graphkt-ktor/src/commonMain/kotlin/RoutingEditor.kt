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
import org.cufy.graphkt.ktor.internal.graphktStaticResource

private const val GRAPHIQL = "graphkt-graphiql.html"
private const val GRAPHQL_PLAYGROUND = "graphkt-graphql-playground.html"
private const val APOLLO_SANDBOX = "graphkt-apollo-sandbox.html"

// Playground

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql playground
 * html.
 */
@Deprecated("Has Being Replaced", ReplaceWith("graphqlPlayground(path)"))
@KtorDsl
fun Application.playground(path: String = "/graphql") = graphqlPlayground(path)

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql playground
 * html.
 */
@Deprecated("Has Being Replaced", ReplaceWith("graphqlPlayground(path)"))
@KtorDsl
fun Route.playground(path: String = "/graphql") = graphqlPlayground(path)

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql playground
 * html.
 */
@KtorDsl
fun Application.graphqlPlayground(path: String = "/graphql") =
    routing { graphktStaticResource(path, GRAPHQL_PLAYGROUND) }

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql playground
 * html.
 */
@KtorDsl
fun Route.graphqlPlayground(path: String = "/graphql") =
    graphktStaticResource(path, GRAPHQL_PLAYGROUND)

// Sandbox

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql sandbox
 * html.
 */
@Deprecated("Has Being Replaced", ReplaceWith("apolloSandbox(path)"))
@KtorDsl
fun Application.sandbox(path: String = "/graphql") = apolloSandbox(path)

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql sandbox
 * html.
 */
@Deprecated("Has Being Replaced", ReplaceWith("apolloSandbox(path)"))
@KtorDsl
fun Route.sandbox(path: String = "/graphql") = apolloSandbox(path)

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql sandbox
 * html.
 */
@KtorDsl
fun Application.apolloSandbox(path: String = "/graphql") =
    routing { graphktStaticResource(path, APOLLO_SANDBOX) }

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphql sandbox
 * html.
 */
@KtorDsl
fun Route.apolloSandbox(path: String = "/graphql") =
    graphktStaticResource(path, APOLLO_SANDBOX)

// graphiql

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphiql html.
 */
@KtorDsl
fun Application.graphiql(path: String = "/graphql") =
    routing { graphktStaticResource(path, GRAPHIQL) }

/**
 * Builds a route to match graphql `GET` requests
 * and respond to them with the graphiql html.
 */
@KtorDsl
fun Route.graphiql(path: String = "/graphql") =
    graphktStaticResource(path, GRAPHIQL)
