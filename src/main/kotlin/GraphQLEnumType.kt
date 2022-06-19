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
open class GraphQLEnumTypeScope(
    name: String? = null,
    /**
     * The wrapped builder.
     *
     * @since 1.0.0
     */
    val builder: GraphQLEnumType.Builder =
        GraphQLEnumType.newEnum()
            .apply { name?.let { name(it) } }
) {
    /**
     * The name of the enum type.
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
     * The description of the enum type.
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
     * Define a value for this enum type.
     *
     * @since 1.0.0
     */
    fun value(
        name: String? = null,
        value: Any? = null,
        block: GraphQLEnumValueDefinitionScope.() -> Unit = {}
    ) {
        builder.value(GraphQLEnumValueDefinition(name, value, block))
    }
}

/**
 * Create a new [GraphQLEnumType] and apply the
 * given [block] to it.
 *
 * @since 1.0.0
 */
inline fun GraphQLEnumType(
    name: String? = null,
    block: GraphQLEnumTypeScope.() -> Unit = {}
): GraphQLEnumType {
    return GraphQLEnumTypeScope(name)
        .apply(block)
        .builder
        .build()
}
