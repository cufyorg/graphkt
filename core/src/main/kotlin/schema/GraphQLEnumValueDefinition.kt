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
import org.cufy.graphkt.internal.GraphQLEnumValueDefinitionBuilderImpl

/* ============= ------------------ ============= */

/**
 * A definition of an enum value.
 *
 * @param T the runtime value type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLEnumValueDefinition<T> : WithName, WithDirectives {
    /**
     * The enum's runtime value.
     */
    val value: T
}

/**
 * A block of code invoked to fill in options in
 * [GraphQLEnumValueDefinitionBuilder].
 */
typealias GraphQLEnumValueDefinitionBlock<T> =
        GraphQLEnumValueDefinitionBuilder<T>.() -> Unit

/**
 * A block of code invoked to fill in options in
 * [GraphQLEnumValueDefinitionBuilder].
 */
@Deprecated("Use shorter name instead", ReplaceWith(
    "GraphQLEnumValueDefinitionBlock<T>",
    "org.cufy.graphkt.schema.GraphQLEnumValueDefinitionBlock"
))
typealias GraphQLEnumValueDefinitionBuilderBlock<T> =
        GraphQLEnumValueDefinitionBuilder<T>.() -> Unit

/**
 * A builder for creating a [GraphQLEnumValueDefinition].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLEnumValueDefinitionBuilder<T> :
    WithNameBuilder,
    WithDirectivesBuilder,
    WithDeferredBuilder {

    /**
     * The runtime value.
     *
     * @since 2.0.0
     */
    @AdvancedGraphktApi("Use `value()` instead")
    var value: Lazy<T>? // REQUIRED

    /**
     * Build the definition.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLEnumValueDefinition<T>
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLEnumValueDefinitionBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLEnumValueDefinitionBuilder(): GraphQLEnumValueDefinitionBuilder<T> {
    return GraphQLEnumValueDefinitionBuilderImpl()
}

/**
 * Construct a new [GraphQLEnumValueDefinition] with the
 * given [block].
 *
 * @param name the initial name.
 * @param value the initial value. (null if unset)
 * @param block the builder block.
 * @return a new field definition.
 * @since 2.0.0
 */
fun <T> GraphQLEnumValueDefinition(
    name: String? = null,
    value: T? = null,
    block: GraphQLEnumValueDefinitionBlock<T> = {}
): GraphQLEnumValueDefinition<T> {
    val builder = GraphQLEnumValueDefinitionBuilder<T>()
    name?.let { builder.name(it) }
    value?.let { builder.value { it } }
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */

// value

/**
 * Set the enum's value to be the result of
 * invoking the given [block].
 */
@OptIn(AdvancedGraphktApi::class)
fun <T> GraphQLEnumValueDefinitionBuilder<T>.value(
    block: () -> T
) {
    value = lazy(block)
}

/* ============= ------------------ ============= */
