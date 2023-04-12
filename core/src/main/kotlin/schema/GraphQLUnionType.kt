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
package org.cufy.graphkt.schema

import org.cufy.graphkt.AdvancedGraphktApi
import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.internal.GraphQLUnionTypeBuilderImpl

/* ============= ------------------ ============= */

/**
 * A type for GraphQL unions.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLUnionType<T : Any> : WithName, WithDirectives, GraphQLOutputType<T> {
    /**
     * The wrapped types.
     *
     * @since 2.0.0
     */
    val types: List<GraphQLObjectType<out T>>

    /**
     * The type getter.
     *
     * @since 2.0.0
     */
    val typeGetter: GraphQLTypeGetter<T>
}

/**
 * A block of code invoked to fill in options in
 * [GraphQLUnionTypeBuilder].
 */
typealias GraphQLUnionTypeBlock<T> =
        GraphQLUnionTypeBuilder<T>.() -> Unit

/**
 * A block of code invoked to fill in options in
 * [GraphQLUnionTypeBuilder].
 */
@Deprecated("Use shorter name instead", ReplaceWith(
    "GraphQLUnionTypeBlock<T>",
    "org.cufy.graphkt.schema.GraphQLUnionTypeBlock"
))
typealias GraphQLUnionTypeBuilderBlock<T> =
        GraphQLUnionTypeBuilder<T>.() -> Unit

/**
 * A builder for creating a [GraphQLUnionType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLUnionTypeBuilder<T : Any> :
    WithNameBuilder,
    WithTypeGetterBuilder<T>,
    WithDirectivesBuilder,
    WithDeferredBuilder {

    /**
     * The types.
     */
    @AdvancedGraphktApi("Use `type()` instead.")
    val types: MutableList<GraphQLObjectType<out T>>

    /**
     * Build the type.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLUnionType<T>
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLUnionTypeBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T : Any> GraphQLUnionTypeBuilder(): GraphQLUnionTypeBuilder<T> {
    return GraphQLUnionTypeBuilderImpl()
}

/**
 * Construct a new [GraphQLUnionType] with the
 * given [block].
 *
 * @param types the initial types.
 * @param block the builder block.
 * @return a new union type.
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any> GraphQLUnionType(
    name: String? = null,
    vararg types: GraphQLObjectType<out T>,
    block: GraphQLUnionTypeBlock<T>
): GraphQLUnionType<T> {
    val builder = GraphQLUnionTypeBuilder<T>()
    name?.let { builder.name(it) }
    builder.types += types
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */

// types

/**
 * Add the given [type] to the union.
 *
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any> GraphQLUnionTypeBuilder<T>.type(
    type: GraphQLObjectType<out T>
) {
    types += type
}
