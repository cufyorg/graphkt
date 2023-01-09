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
package org.cufy.graphkt.java

import graphql.schema.idl.SchemaPrinter
import kotlinx.coroutines.flow.Flow
import org.cufy.graphkt.*
import org.cufy.graphkt.java.internal.GraphQLResponse
import org.cufy.graphkt.java.internal.JavaExecutionInput
import org.cufy.graphkt.java.internal.JavaGraphQLSchema
import org.cufy.graphkt.schema.GraphQLRequest
import org.cufy.graphkt.schema.GraphQLResponse
import org.cufy.graphkt.schema.GraphQLSchema
import java.io.PrintStream
import graphql.GraphQL as JavaGraphQL

/* ============= - EngineFactory  - ============= */

/**
 * The `graphql-java` implementation of [GraphktEngineFactory].
 *
 * @author LSafer
 * @since 2.0.0
 */
object GraphQLJava : GraphktEngineFactory<GraphQLJavaConfiguration> {
    @OptIn(AdvancedGraphktApi::class, InternalGraphktApi::class)
    override fun invoke(
        schema: GraphQLSchema,
        block: GraphQLJavaConfiguration.() -> Unit
    ): GraphktEngine {
        val configuration = GraphQLJavaConfiguration()
        configuration.apply(block)
        configuration.deferred.forEach { it() }
        configuration.deferred.clear()

        val schemaBlock = configuration.schemaBlock.toList()
        val graphqlBlock = configuration.graphqlBlock.toList()

        val javaSchema = JavaGraphQLSchema(schema)
            .transform { s -> schemaBlock.forEach { it(s) } }

        val graphql = JavaGraphQL.newGraphQL(javaSchema)
            .build()
            .transform { g -> graphqlBlock.forEach { it(g) } }

        return GraphQLJavaEngine(graphql, configuration)
    }
}

/* ============= ----- Engine ----- ============= */

/**
 * The `graphql-java` implementation of [GraphktEngine].
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLJavaEngine(
    /**
     * The graphql instance.
     */
    val graphql: JavaGraphQL,
    /**
     * The configuration.
     */
    configuration: GraphQLJavaConfiguration
) : GraphktEngine {
    @OptIn(AdvancedGraphktApi::class)
    private val executionInputBlock = configuration.executionInputBlock.toList()

    override fun printSchema(out: PrintStream) {
        val printer = SchemaPrinter()
        val result = printer.print(graphql.graphQLSchema)

        out.print(result)
    }

    @OptIn(InternalGraphktApi::class)
    override fun execute(
        request: GraphQLRequest,
        context: Map<Any?, Any?>,
        local: Map<Any?, Any?>
    ): Flow<GraphQLResponse> {
        val input = JavaExecutionInput(request, context, local)
            .transform { i -> executionInputBlock.forEach { it(i) } }

        val result = graphql.execute(input)

        return GraphQLResponse(result)
    }
}
