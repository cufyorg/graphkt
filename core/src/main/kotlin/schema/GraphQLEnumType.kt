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
import org.cufy.graphkt.internal.GraphQLEnumTypeBuilderImpl

/* ============= ------------------ ============= */

/**
 * The type of a graphql enum.
 *
 * @param T the enum's runtime value.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLEnumType<T> : WithName, WithDirectives, GraphQLInputOutputType<T> {
    /**
     * The enum's values.
     */
    val values: List<GraphQLEnumValueDefinition<T>>
}

/**
 * A block of code invoked to fill in options in
 * [GraphQLEnumTypeBuilder].
 */
typealias GraphQLEnumTypeBuilderBlock<T> =
        GraphQLEnumTypeBuilder<T>.() -> Unit

/**
 * A builder for creating a [GraphQLEnumType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLEnumTypeBuilder<T> :
    WithNameBuilder,
    WithDirectivesBuilder,
    WithDeferredBuilder {

    /**
     * The enum's values.
     */
    @AdvancedGraphktApi("Use `value()` instead")
    val values: MutableList<GraphQLEnumValueDefinition<T>>

    /**
     * Build the type.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLEnumType<T>
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLEnumTypeBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLEnumTypeBuilder(): GraphQLEnumTypeBuilder<T> {
    return GraphQLEnumTypeBuilderImpl()
}

/**
 * Construct a new [GraphQLEnumType] with the
 * given [block].
 *
 * @param name the initial name.
 * @param block the builder block.
 * @return a new enum type.
 * @since 2.0.0
 */
fun <T> GraphQLEnumType(
    name: String? = null,
    block: GraphQLEnumTypeBuilderBlock<T> = {}
): GraphQLEnumType<T> {
    val builder = GraphQLEnumTypeBuilder<T>()
    name?.let { builder.name(it) }
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */

// value

/**
 * Add the given value [definition].
 */
@OptIn(AdvancedGraphktApi::class)
fun <T> GraphQLEnumTypeBuilder<T>.value(
    definition: GraphQLEnumValueDefinition<T>
) {
    values += definition
}

/**
 * Add a value definition with the given arguments.
 *
 * @param name the initial name.
 * @param value the initial value. (null if unset)
 * @param block the builder block.
 * @since 2.0.0
 */
fun <T> GraphQLEnumTypeBuilder<T>.value(
    name: String? = null,
    value: T? = null,
    block: GraphQLEnumValueDefinitionBuilderBlock<T> = {}
) {
    value(GraphQLEnumValueDefinition(name, value, block))
}

/* ============= ------------------ ============= */
