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
import org.cufy.graphkt.ExperimentalGraphktApi
import org.cufy.graphkt.GraphQLEngine
import org.cufy.graphkt.GraphQLEngineFactory
import org.cufy.graphkt.GraphQLMutableElementWithEngine
import org.cufy.graphkt.java.internal.JavaGraphQL
import org.cufy.graphkt.java.internal.createJavaExecutionInput
import org.cufy.graphkt.java.internal.transformGraphQLSchema
import org.cufy.graphkt.java.internal.transformToGraphQLResponseFlow
import org.cufy.graphkt.schema.GraphQLRequest
import org.cufy.graphkt.schema.GraphQLResponse
import org.cufy.graphkt.schema.GraphQLSchema
import java.io.Reader

// GraphQL Java Engine Factory

@ExperimentalGraphktApi
private val singletonOfGraphQLJava by lazy { GraphQLJava { } }

/**
 * The `graphql-java` implementation of [GraphQLEngineFactory].
 *
 * @author LSafer
 * @since 2.0.0
 */
@ExperimentalGraphktApi
class GraphQLJava(
    @Suppress("MemberVisibilityCanBePrivate")
    val configuration: GraphQLJavaConfiguration
) : GraphQLEngineFactory {
    override fun invoke(schema: GraphQLSchema): GraphQLEngine {
        val javaSchema = transformGraphQLSchema(schema)
            .transform { configuration.schemaTransformers.forEach { block -> block(it) } }

        val graphql = JavaGraphQL.newGraphQL(javaSchema)
            .build()
            .transform { configuration.graphqlTransformers.forEach { block -> block(it) } }

        return GraphQLJavaEngine(graphql, configuration)
    }
}

/**
 * Obtain the default `graphql-java` engine factory.
 */
@ExperimentalGraphktApi
fun GraphQLJava(): GraphQLJava {
    return singletonOfGraphQLJava
}

/**
 * Create a new [singletonOfGraphQLJava] instance configured
 * with the given configuration [block].
 */
@ExperimentalGraphktApi
fun GraphQLJava(block: GraphQLJavaConfigurationBlock): GraphQLJava {
    return GraphQLJava(GraphQLJavaConfiguration(block))
}

// GraphQL Java Engine

/**
 * The `graphql-java` implementation of [GraphQLEngine].
 *
 * @author LSafer
 * @since 2.0.0
 */
@ExperimentalGraphktApi
class GraphQLJavaEngine(
    /**
     * The graphql instance.
     */
    val graphql: JavaGraphQL,
    /**
     * The configuration.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val configuration: GraphQLJavaConfiguration
) : GraphQLEngine {
    override fun obtainSchemaReader(): Reader {
        val printer = SchemaPrinter()
        val out = printer.print(graphql.graphQLSchema)
        return out.reader()
    }

    override fun execute(
        request: GraphQLRequest,
        context: Map<Any?, Any?>,
        local: Map<Any?, Any?>
    ): Flow<GraphQLResponse> {
        val input = createJavaExecutionInput(request, context, local)
            .transform { configuration.executionInputTransformers.forEach { block -> block(it) } }

        val resultFlow = graphql.executeAsync(input).asDeferred()

        return flow {
            val result = resultFlow.await()

            emitAll(transformToGraphQLResponseFlow(result))
        }
    }
}

// Extensions

/**
 * Set the engine factory to be [GraphQLJava].
 *
 * @param block the engine configuration.
 * @since 2.0.0
 */
@OptIn(ExperimentalGraphktApi::class)
@Suppress("FunctionName")
fun GraphQLMutableElementWithEngine.`graphql-java`(block: GraphQLJavaConfigurationBlock) {
    engine = GraphQLJava(block)
}

/**
 * Set the engine factory to be [singletonOfGraphQLJava].
 *
 * @since 2.0.0
 */
@OptIn(ExperimentalGraphktApi::class)
@Suppress("ObjectPropertyName")
val GraphQLMutableElementWithEngine.`graphql-java`: Unit
    get() = run { engine = GraphQLJava() }
