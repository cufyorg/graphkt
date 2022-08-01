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

import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLOutputType
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KProperty1

/**
 * A kotlin-friendly wrapper over [GraphQLFieldDefinition.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLFieldDefinitionBuilder<O, T> :
    GraphQLFieldDefinition.Builder() {
    /**
     * The name of the field.
     *
     * @since 1.0.0
     */
    var name: String
        get() = super.name
        set(value) = run { super.name = value }

    /**
     * The description of the field.
     *
     * @since 1.0.0
     */
    var description: String
        get() = super.description
        set(value) = run { super.description = value }

    /**
     * If deprecated, the deprecation reason of the field.
     *
     * @since 1.0.0
     */
    var deprecationReason: String
        @Deprecated(
            "builder.deprecationReason is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = error("builder.deprecationReason is not accessible")
        set(value) = run { deprecate(value) }

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
        get() = error("builder.type is not accessible")
        set(value) = run { super.type(value) }
}

/**
 * Create a new field definition delegating to the
 * given [field].
 *
 * @since 1.0.0
 */
inline fun <O, T> GraphQLFieldDefinition(
    field: KProperty1<in O, T>,
    type: GraphQLOutputType? = null,
    block: GraphQLFieldDefinitionBuilder<O, T>.() -> Unit = {}
): GraphQLFieldDefinition {
    return GraphQLFieldDefinition(field.name, type) {
        resolver { field.get(it) }
        block()
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
    block: GraphQLFieldDefinitionBuilder<O, T>.() -> Unit = {}
): GraphQLFieldDefinition {
    val builder = GraphQLFieldDefinitionBuilder<O, T>()
    name?.let { builder.name = it }
    type?.let { builder.type = it }
    builder.apply(block)
    return builder.build()
}

/**
 * Set the field type to the result of [block].
 *
 * @since 1.1.0
 */
fun GraphQLFieldDefinitionBuilder<*, *>.type(
    block: () -> GraphQLOutputType
) {
    type = block()
}

/**
 * Define an argument for this field.
 *
 * @return an accessor to the argument to be
 *         used in an accessor block.
 * @since 1.0.0
 */
fun <A> GraphQLFieldDefinitionBuilder<*, *>.argument(
    name: String? = null,
    type: GraphQLInputType? = null,
    block: GraphQLArgumentBuilder<A>.() -> Unit = {}
): ResolverScope.() -> A {
    val argument = GraphQLArgument(name, type, block)
    argument(argument)
    return { getArgument(argument.name) }
}

/**
 * Define a resolver for this field.
 *
 * @since 1.0.0
 */
fun <O, T> GraphQLFieldDefinitionBuilder<O, T>.resolver(
    block: suspend ResolverScope.(O) -> T
) {
    @Suppress("DEPRECATION")
    dataFetcher { scope ->
        CompletableFuture.supplyAsync {
            runBlocking {
                block(scope, scope.getSource())
            }
        }
    }
}
