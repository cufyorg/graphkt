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
import graphql.schema.GraphQLInputObjectField
import graphql.schema.GraphQLInputType

/**
 * A kotlin-friendly wrapper over [GraphQLInputObjectField.Builder].
 *
 * Note: Unlike [GraphQLFieldDefinition], this does not
 *  get converted into a property. Instead, will be
 *  a key-value pair at runtime.
 *
 * @author LSafer
 * @since 1.1.0
 */
open class GraphQLInputObjectFieldBuilder :
    GraphQLInputObjectField.Builder() {
    /**
     * The name of the field.
     *
     * @since 1.1.0
     */
    var name: String
        get() = super.name
        set(value) = run { super.name = value }

    /**
     * The description of the field.
     *
     * @since 1.1.0
     */
    var description: String
        get() = super.description
        set(value) = run { super.description = value }

    /**
     * If deprecated, the deprecation reason of the field.
     *
     * @since 1.1.0
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
     * @since 1.1.0
     */
    var type: GraphQLInputType
        @Deprecated(
            "builder.type is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = error("builder.type is not accessible")
        set(value) = run { super.type(value) }
}

// Fields

/**
 * Set the name to the result of [block].
 *
 * @since 1.1.0
 */
fun GraphQLInputObjectFieldBuilder.name(
    block: () -> String
) {
    this.name = block()
}

/**
 * Set the description to the result of [block].
 *
 * @since 1.1.0
 */
fun GraphQLInputObjectFieldBuilder.description(
    block: () -> String
) {
    this.description = block()
}

/**
 * Set the description to the result of [block].
 *
 * @since 1.1.0
 */
fun GraphQLInputObjectFieldBuilder.deprecation(
    block: () -> String
) {
    this.deprecationReason = block()
}

/**
 * Set the field type to the result of [block].
 *
 * @since 1.1.0
 */
fun GraphQLInputObjectFieldBuilder.type(
    block: () -> GraphQLInputType
) {
    type = block()
}

// Constructors

/**
 * Create a new [GraphQLInputObjectField] and apply the
 * given [block] to it.
 *
 * Note: Unlike [GraphQLFieldDefinition], this does not
 *  get converted into a property. Instead, will be
 *  a key-value pair at runtime.
 *
 * @since 1.1.0
 */
inline fun GraphQLInputObjectField(
    name: String? = null,
    type: GraphQLInputType? = null,
    block: GraphQLInputObjectFieldBuilder.() -> Unit = {}
): GraphQLInputObjectField {
    val builder = GraphQLInputObjectFieldBuilder()
    name?.let { builder.name = it }
    type?.let { builder.type = it }
    builder.apply(block)
    return builder.build()
}
