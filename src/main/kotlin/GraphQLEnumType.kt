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

import graphql.schema.GraphQLEnumType

/**
 * A kotlin-friendly wrapper over [GraphQLEnumType.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLEnumTypeBuilder<T> :
    GraphQLEnumType.Builder() {
    /**
     * The name of the enum type.
     *
     * @since 1.0.0
     */
    var name: String
        get() = super.name
        set(value) = run { super.name = value }

    /**
     * The description of the enum type.
     *
     * @since 1.0.0
     */
    var description: String
        get() = super.description
        set(value) = run { super.description = value }
}

// Fields

/**
 * Set the name to the result of [block].
 *
 * @since 1.1.0
 */
fun GraphQLEnumTypeBuilder<*>.name(
    block: () -> String
) {
    this.name = block()
}

/**
 * Set the description to the result of [block].
 *
 * @since 1.1.0
 */
fun GraphQLEnumTypeBuilder<*>.description(
    block: () -> String
) {
    this.description = block()
}

// Constructors

/**
 * Create a new [GraphQLEnumType] and apply the
 * given [block] to it.
 *
 * @since 1.0.0
 */
inline fun <T> GraphQLEnumType(
    name: String? = null,
    block: GraphQLEnumTypeBuilder<T>.() -> Unit = {}
): GraphQLEnumType {
    val builder = GraphQLEnumTypeBuilder<T>()
    name?.let { builder.name = it }
    builder.apply(block)
    return builder.build()
}

// Extensions

/**
 * Define a value for this enum type.
 *
 * @since 1.0.0
 */
fun <T : Enum<T>> GraphQLEnumTypeBuilder<T>.value(
    enum: T,
    block: GraphQLEnumValueDefinitionBuilder<T>.() -> Unit = {}
) {
    value(GraphQLEnumValueDefinition(enum, block))
}

/**
 * Define a value for this enum type.
 *
 * @since 1.0.0
 */
fun <T> GraphQLEnumTypeBuilder<T>.value(
    name: String? = null,
    value: T? = null,
    block: GraphQLEnumValueDefinitionBuilder<T>.() -> Unit = {}
) {
    value(GraphQLEnumValueDefinition(name, value, block))
}
