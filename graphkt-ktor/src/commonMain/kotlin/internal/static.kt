/*
 *	Copyright 2022-2024 cufy.org and meemer.com
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

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.cufy.graphkt.ktor.GraphQLKtorConfiguration

internal fun Route.graphktStaticResource(path: String, name: String) {
    val classloader = GraphQLKtorConfiguration::class.java.classLoader
    val resource = classloader.getResource(name)!!

    get(path) {
        call.respondOutputStream(ContentType.Text.Html) {
            val input = withContext(Dispatchers.IO) {
                resource.openStream()
            }

            input.use { it.copyTo(this) }
        }
    }
}
