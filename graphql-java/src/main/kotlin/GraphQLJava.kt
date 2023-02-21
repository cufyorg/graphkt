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
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.future.asDeferred
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

private lateinit var singletonOfGraphQLJava: GraphQLJava

/**
 * The `graphql-java` implementation of [GraphktEngineFactory].
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLJava(
    val configuration: GraphQLJavaConfiguration
) : GraphktEngineFactory {
    @OptIn(InternalGraphktApi::class)
    override fun invoke(schema: GraphQLSchema): GraphktEngine {
        val javaSchema = JavaGraphQLSchema(schema)
            .transform { configuration.schemaBlock(it) }

        val graphql = JavaGraphQL.newGraphQL(javaSchema)
            .build()
            .transform { configuration.graphqlBlock(it) }

        return GraphQLJavaEngine(graphql, configuration)
    }
}

/**
 * Obtain the default `graphql-java` engine factory.
 */
fun GraphQLJava(): GraphQLJava {
    if (!::singletonOfGraphQLJava.isInitialized)
        singletonOfGraphQLJava = GraphQLJava { }

    return singletonOfGraphQLJava
}

/**
 * Create a new [singletonOfGraphQLJava] instance configured
 * with the given configuration [block].
 */
fun GraphQLJava(block: GraphQLJavaConfigurationBuilder.() -> Unit): GraphQLJava {
    return GraphQLJava(GraphQLJavaConfiguration(block))
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
    val configuration: GraphQLJavaConfiguration
) : GraphktEngine {
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
            .transform { configuration.executionInputBlock(it) }

        val resultFlow = graphql.executeAsync(input).asDeferred()

        return flow {
            val result = resultFlow.await()

            emitAll(GraphQLResponse(result))
        }
    }
}

/**
 * Set the engine factory to be [GraphQLJava].
 *
 * @param block the engine configuration.
 * @since 2.0.0
 */
@Suppress("FunctionName")
fun WithEngine.`graphql-java`(block: GraphQLJavaConfigurationBuilder.() -> Unit) {
    engine = GraphQLJava(block)
}

/**
 * Set the engine factory to be [singletonOfGraphQLJava].
 *
 * @since 2.0.0
 */
@Suppress("ObjectPropertyName")
val WithEngine.`graphql-java`: Unit
    get() = run { engine = GraphQLJava() }
