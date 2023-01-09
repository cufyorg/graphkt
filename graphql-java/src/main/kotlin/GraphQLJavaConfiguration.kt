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
 * The configuration for creating graphql-java engine.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLJavaConfiguration : WithDeferredBuilder {
    /**
     * Code to be executed to transform graphql schema.
     */
    @AdvancedGraphktApi("Use `transformSchema()` instead")
    val schemaBlock: MutableList<GraphQLSchema.Builder.() -> Unit> = mutableListOf()

    /**
     * Code to be executed to transform graphql instance.
     */
    @AdvancedGraphktApi("Use `transformInstance()` instead")
    val graphqlBlock: MutableList<GraphQL.Builder.() -> Unit> = mutableListOf()

    /**
     * Code to be executed to transform execution input.
     */
    @AdvancedGraphktApi("Use `transformInput()` instead")
    val executionInputBlock: MutableList<ExecutionInput.Builder.() -> Unit> = mutableListOf()

    @AdvancedGraphktApi("Use `deferred()` instead")
    override val deferred: MutableList<() -> Unit> = mutableListOf()
}

// schemaBlock

/**
 * Add the given [block] to configure the
 * `java-graphql` schema after being translated
 * and before being used.
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLJavaConfiguration.transformSchema(
    block: GraphQLSchema.Builder.() -> Unit
) {
    schemaBlock += block
}

// graphqlBlock

/**
 * Add the given [block] to configure the
 * `java-graphql` instance after being translated
 * and before being used.
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLJavaConfiguration.transformInstance(
    block: GraphQL.Builder.() -> Unit
) {
    graphqlBlock += block
}

// executionInputBlock

/**
 * Add the given [block] to configure the
 * `java-graphql` input after being translated
 * and before being used.
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLJavaConfiguration.transformInput(
    block: ExecutionInput.Builder.() -> Unit
) {
    executionInputBlock += block
}
