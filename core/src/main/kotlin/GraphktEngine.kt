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
package org.cufy.graphkt

import kotlinx.coroutines.flow.Flow
import org.cufy.graphkt.schema.GraphQLRequest
import org.cufy.graphkt.schema.GraphQLResponse
import org.cufy.graphkt.schema.GraphQLSchema
import java.io.PrintStream

/**
 * An instance used to construct new graphql engines.
 *
 * @param TConfiguration the engine's configuration type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphktEngineFactory<TConfiguration> {
    /**
     * Create a new engine instance.
     *
     * @param schema the engine's schema.
     * @param block the configuration block.
     * @since 2.0.0
     */
    operator fun invoke(
        schema: GraphQLSchema,
        block: TConfiguration.() -> Unit = {}
    ): GraphktEngine
}

/**
 * An instance used to actually execute graphql
 * requests.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphktEngine {
    /**
     * Print the schema to the given [out].
     *
     * @since 2.0.0
     */
    fun printSchema(out: PrintStream = System.out)

    /**
     * Execute the given [request].
     *
     * @param request the request to execute.
     * @param context the context variables.
     * @param local the initial local.
     * @since 2.0.0
     */
    fun execute(
        request: GraphQLRequest,
        context: Map<Any?, Any?> = emptyMap(),
        local: Map<Any?, Any?> = emptyMap()
    ): Flow<GraphQLResponse>
}
