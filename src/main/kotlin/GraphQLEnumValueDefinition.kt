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

import graphql.schema.GraphQLEnumValueDefinition

/**
 * A kotlin-friendly wrapper over [GraphQLEnumValueDefinition.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLEnumValueDefinitionBuilder<T> :
    GraphQLEnumValueDefinition.Builder() {
    /**
     * The name of the value.
     *
     * @since 1.0.0
     */
    var name: String
        get() = super.name
        set(value) = run { super.name = value }

    /**
     * The description of the value.
     *
     * @since 1.0.0
     */
    var description: String
        get() = super.description
        set(value) = run { super.description = value }

    /**
     * If deprecated, the deprecation reason of the value.
     *
     * @since 1.0.0
     */
    var deprecationReason: String
        @Deprecated(
            "builder.deprecationReason is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = error("builder.deprecationReason is not accessible")
        set(value) = run { super.deprecationReason(value) }

    /**
     * The runtime value of the value.
     *
     * @since 1.0.0
     */
    var value: T
        @Deprecated(
            "builder.value is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = error("builder.value is not accessible")
        set(value) = run { super.value(value) }
}

// Constructors

/**
 * Create a new [GraphQLEnumValueDefinition] and
 * apply the given [block] to it.
 *
 * @since 1.0.0
 */
fun <T : Enum<T>> GraphQLEnumValueDefinition(
    enum: T,
    block: GraphQLEnumValueDefinitionBuilder<T>.() -> Unit = {}
): GraphQLEnumValueDefinition {
    return GraphQLEnumValueDefinition(
        enum.name, enum, block
    )
}

/**
 * Create a new [GraphQLEnumValueDefinition] and
 * apply the given [block] to it.
 *
 * @since 1.0.0
 */
fun <T> GraphQLEnumValueDefinition(
    name: String? = null,
    value: T? = null,
    block: GraphQLEnumValueDefinitionBuilder<T>.() -> Unit = {}
): GraphQLEnumValueDefinition {
    val builder = GraphQLEnumValueDefinitionBuilder<T>()
    name?.let { builder.name = it }
    value?.let { builder.value = it }
    builder.apply(block)
    return builder.build()
}
