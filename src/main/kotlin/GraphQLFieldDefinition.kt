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
package org.cufy.kaguya

import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLOutputType
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

/**
 * A kotlin-friendly wrapper over [GraphQLFieldDefinition.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLFieldDefinitionScope<O, T>(
    name: String? = null,
    type: GraphQLOutputType? = null,
    /**
     * The wrapped builder.
     *
     * @since 1.0.0
     */
    val builder: GraphQLFieldDefinition.Builder =
        GraphQLFieldDefinition.newFieldDefinition()
            .apply { name?.let { name(it) } }
            .apply { type?.let { type(it) } }
) {
    /**
     * The name of the field.
     *
     * @since 1.0.0
     */
    var name: String
        @Deprecated(
            "builder.name is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = TODO("builder.name is not accessible")
        set(value) = run { builder.name(value) }

    /**
     * The description of the field.
     *
     * @since 1.0.0
     */
    var description: String
        @Deprecated(
            "builder.description is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = TODO("builder.description is not accessible")
        set(value) = run { builder.description(value) }

    /**
     * The type of the field.
     *
     * @since 1.0.0
     */
    var type: GraphQLOutputType
        @Deprecated(
            "builder.type is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = TODO("builder.type is not accessible")
        set(value) = run { builder.type(value) }

    /**
     * Define an argument for this field.
     *
     * @return an accessor to the argument to be
     *         used in an accessor block.
     * @since 1.0.0
     */
    fun <A> argument(
        name: String? = null,
        type: GraphQLInputType? = null,
        block: GraphQLArgumentScope<A>.() -> Unit = {}
    ): DataFetchingEnvironment.() -> A {
        val argument = GraphQLArgument(name, type, block)
        builder.argument(argument)
        return { getArgument(argument.name) }
    }

    /**
     * Define a resolver for this field.
     *
     * @since 1.0.0
     */
    fun resolver(
        block: suspend DataFetchingEnvironment.(O) -> T
    ) {
        @Suppress("DEPRECATION")
        builder.dataFetcher { environment ->
            CompletableFuture.supplyAsync {
                runBlocking {
                    block(environment, environment.getSource())
                }
            }
        }
    }
}

/**
 * Create a new [GraphQLFieldDefinition] and apply the
 * given [block] to it.
 *
 * @since 1.0.0
 */
inline fun <O, T> GraphQLFieldDefinition(
    name: String? = null,
    type: GraphQLOutputType? = null,
    block: GraphQLFieldDefinitionScope<O, T>.() -> Unit = {}
): GraphQLFieldDefinition {
    return GraphQLFieldDefinitionScope<O, T>(name, type)
        .apply(block)
        .builder
        .build()
}
