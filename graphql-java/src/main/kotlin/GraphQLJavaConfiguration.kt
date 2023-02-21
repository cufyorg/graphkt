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

import graphql.ExecutionInput
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import org.cufy.graphkt.AdvancedGraphktApi
import org.cufy.graphkt.schema.WithDeferredBuilder

/**
 * Configuration of graphql-java engine.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLJavaConfiguration(
    val schemaBlock: GraphQLSchema.Builder.() -> Unit,
    val graphqlBlock: GraphQL.Builder.() -> Unit,
    val executionInputBlock: ExecutionInput.Builder.() -> Unit
)

/**
 * The configuration for creating graphql-java engine.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLJavaConfigurationBuilder : WithDeferredBuilder {
    /**
     * Code to be executed to transform graphql schema.
     */
    @AdvancedGraphktApi("Use `transformSchema()` instead")
    val schemaBlocks: MutableList<GraphQLSchema.Builder.() -> Unit> = mutableListOf()

    /**
     * Code to be executed to transform graphql instance.
     */
    @AdvancedGraphktApi("Use `transformInstance()` instead")
    val graphqlBlocks: MutableList<GraphQL.Builder.() -> Unit> = mutableListOf()

    /**
     * Code to be executed to transform execution input.
     */
    @AdvancedGraphktApi("Use `transformInput()` instead")
    val executionInputBlocks: MutableList<ExecutionInput.Builder.() -> Unit> = mutableListOf()

    @AdvancedGraphktApi("Use `deferred()` instead")
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    /**
     * Build the configuration.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    @OptIn(AdvancedGraphktApi::class)
    fun build(): GraphQLJavaConfiguration {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLJavaConfiguration(
            schemaBlock = schemaBlocks.toList().let {
                { it.forEach { it() } }
            },
            graphqlBlock = graphqlBlocks.toList().let {
                { it.forEach { it() } }
            },
            executionInputBlock = executionInputBlocks.toList().let {
                { it.forEach { it() } }
            }
        )
    }
}

/**
 * Construct a new [GraphQLJavaConfiguration] with
 * the given builder [block].
 *
 * @param block the builder block.
 * @since 2.0.0
 */
fun GraphQLJavaConfiguration(
    block: GraphQLJavaConfigurationBuilder.() -> Unit = {}
): GraphQLJavaConfiguration {
    val builder = GraphQLJavaConfigurationBuilder()
    builder.apply(block)
    return builder.build()
}

// schemaBlock

/**
 * Add the given [block] to configure the
 * `java-graphql` schema after being translated
 * and before being used.
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLJavaConfigurationBuilder.transformSchema(
    block: GraphQLSchema.Builder.() -> Unit
) {
    schemaBlocks += block
}

// graphqlBlock

/**
 * Add the given [block] to configure the
 * `java-graphql` instance after being translated
 * and before being used.
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLJavaConfigurationBuilder.transformInstance(
    block: GraphQL.Builder.() -> Unit
) {
    graphqlBlocks += block
}

// executionInputBlock

/**
 * Add the given [block] to configure the
 * `java-graphql` input after being translated
 * and before being used.
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLJavaConfigurationBuilder.transformInput(
    block: ExecutionInput.Builder.() -> Unit
) {
    executionInputBlocks += block
}
