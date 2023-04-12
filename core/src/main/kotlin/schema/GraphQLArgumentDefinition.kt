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

import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.internal.GraphQLArgumentDefinitionBuilderImpl
import org.cufy.graphkt.internal.GraphQLArgumentImpl

/* ============= ------------------ ============= */

/**
 * A definition of an argument.
 *
 * @param T the type of the argument's value.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLArgumentDefinition<T> : WithName, WithDirectives {
    /**
     * The type of the value of the argument.
     */
    val type: GraphQLInputType<T>
}

/**
 * A block of code invoked to fill in options in
 * [GraphQLArgumentDefinitionBuilder].
 */
typealias GraphQLArgumentDefinitionBlock<T> =
        GraphQLArgumentDefinitionBuilder<T>.() -> Unit

/**
 * A block of code invoked to fill in options in
 * [GraphQLArgumentDefinitionBuilder].
 */
@Deprecated("Use shorter name instead", ReplaceWith(
    "GraphQLArgumentDefinitionBlock<T>",
    "org.cufy.graphkt.schema.GraphQLArgumentDefinitionBlock"
))
typealias GraphQLArgumentDefinitionBuilderBlock<T> =
        GraphQLArgumentDefinitionBuilder<T>.() -> Unit

/**
 * A builder for creating a [GraphQLArgumentDefinition].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLArgumentDefinitionBuilder<T> :
    WithNameBuilder,
    WithDirectivesBuilder,
    WithInputTypeBuilder<T>,
    WithDeferredBuilder {

    /**
     * Build the definition.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLArgumentDefinition<T>
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLArgumentDefinitionBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLArgumentDefinitionBuilder(): GraphQLArgumentDefinitionBuilder<T> {
    return GraphQLArgumentDefinitionBuilderImpl()
}

/**
 * Construct a new [GraphQLArgumentDefinition] with the
 * given [block].
 *
 * @param name the initial name.
 * @param type the initial type.
 * @param block the builder block.
 * @return a new argument definition.
 * @since 2.0.0
 */
fun <T> GraphQLArgumentDefinition(
    name: String? = null,
    type: GraphQLInputType<T>? = null,
    block: GraphQLArgumentDefinitionBlock<T> = {}
): GraphQLArgumentDefinition<T> {
    val builder = GraphQLArgumentDefinitionBuilder<T>()
    name?.let { builder.name(it) }
    type?.let { builder.type { it } }
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */

/**
 * An instance of some argument.
 *
 * @param T the argument's value type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLArgument<T> {
    /**
     * The argument's definition.
     */
    val definition: GraphQLArgumentDefinition<T>

    /**
     * The argument's value.
     */
    val value: T
}

/* ============= ------------------ ============= */

/**
 * Construct a new [GraphQLArgument].
 *
 * @param definition the argument definition.
 * @param value the value.
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLArgument(
    definition: GraphQLArgumentDefinition<T>,
    value: T
): GraphQLArgument<T> {
    return GraphQLArgumentImpl(definition, value)
}
