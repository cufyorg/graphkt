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

import graphql.schema.GraphQLInputObjectType
import graphql.schema.GraphQLInputType

/**
 * A kotlin-friendly wrapper over [GraphQLInputObjectType.Builder].
 *
 * Note: Unlike [GraphQLObjectType], this does not
 *  get converted into objects. Instead, will be
 *  a map at runtime.
 *
 * @author LSafer
 * @since 1.1.0
 */
open class GraphQLInputObjectTypeBuilder :
    GraphQLInputObjectType.Builder() {

    /**
     * The name of the input object type.
     *
     * @since 1.1.0
     */
    var name: String
        get() = super.name
        set(value) = run { super.name = value }

    /**
     * The description of the input object type.
     *
     * @since 1.1.0
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
fun GraphQLInputObjectTypeBuilder.name(
    block: () -> String
) {
    this.name = block()
}

/**
 * Set the description to the result of [block].
 *
 * @since 1.1.0
 */
fun GraphQLInputObjectTypeBuilder.description(
    block: () -> String
) {
    this.description = block()
}

// Constructors

/**
 * Create a new [GraphQLInputObjectType] and apply the
 * given [block] to it.
 *
 * Note: Unlike [GraphQLObjectType], this does not
 *  get converted into objects. Instead, will be
 *  a map at runtime.
 *
 * @since 1.1.0
 */
inline fun GraphQLInputObjectType(
    name: String? = null,
    block: GraphQLInputObjectTypeBuilder.() -> Unit = {}
): GraphQLInputObjectType {
    val builder = GraphQLInputObjectTypeBuilder()
    name?.let { builder.name = it }
    builder.apply(block)
    return builder.build()
}

// Extensions

/**
 * Define a field for this input object type.
 *
 * @since 1.1.0
 */
fun GraphQLInputObjectTypeBuilder.field(
    name: String? = null,
    type: GraphQLInputType? = null,
    block: GraphQLInputObjectFieldBuilder.() -> Unit = {}
) {
    field(GraphQLInputObjectField(name, type, block))
}
