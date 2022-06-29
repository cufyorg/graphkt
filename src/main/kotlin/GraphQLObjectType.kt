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

import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLOutputType
import io.ktor.util.*
import kotlin.reflect.KProperty1

/**
 * A kotlin-friendly wrapper over [GraphQLObjectType.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLObjectTypeBuilder<T> :
    GraphQLObjectType.Builder() {
    /**
     * The name of the object type.
     *
     * @since 1.0.0
     */
    var name: String
        get() = super.name
        set(value) = run { super.name = value }

    /**
     * The description of the object type.
     *
     * @since 1.0.0
     */
    var description: String
        get() = super.description
        set(value) = run { super.description = value }
}

/**
 * Create a new [GraphQLObjectType] and apply the
 * given [block] to it.
 *
 * @since 1.0.0
 */
inline fun <T> GraphQLObjectType(
    name: String? = null,
    block: GraphQLObjectTypeBuilder<T>.() -> Unit = {}
): GraphQLObjectType {
    val builder = GraphQLObjectTypeBuilder<T>()
    name?.let { builder.name = it }
    builder.apply(block)
    return builder.build()
}

/**
 * Define a field for this object type.
 *
 * @param field a kotlin property to get the value
 *              automatically using reflection.
 * @param block a function to configure the field.
 * @since 1.0.0
 */
@KtorDsl
fun <T, M> GraphQLObjectTypeBuilder<T>.field(
    field: KProperty1<in T, M>,
    type: GraphQLOutputType? = null,
    block: GraphQLFieldDefinitionBuilder<T, M>.() -> Unit = {}
) {
    field(GraphQLFieldDefinition(field, type, block))
}

/**
 * Define a field for this object type.
 *
 * @since 1.0.0
 */
@KtorDsl
fun <T, M> GraphQLObjectTypeBuilder<T>.field(
    name: String? = null,
    type: GraphQLOutputType? = null,
    block: GraphQLFieldDefinitionBuilder<T, M>.() -> Unit = {}
) {
    field(GraphQLFieldDefinition(name, type, block))
}
