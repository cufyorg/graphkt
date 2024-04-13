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
package org.cufy.graphkt.ktor.internal

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.serialization.encodeToString
import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.GraphQLRequest
import org.cufy.graphkt.schema.GraphQLResponse

/**
 * Add an HTTP route handling graphql REST requests.
 *
 * This is the actual REST implementation.
 *
 * @param path the route path.
 * @param handler a graphql request handler.
 */
@InternalGraphktApi
internal fun Route.graphqlHttp(
    path: String,
    handler: suspend PipelineContext<Unit, ApplicationCall>.(GraphQLRequest) -> Flow<GraphQLResponse>
) {
    post(path) {
        val request = call.receive<GraphQLRequest>()
        val response = handler(request).single()
        val message = GraphktKtorJson.encodeToString(response)

        call.respond(response)
    }
}
