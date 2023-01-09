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

import org.cufy.graphkt.*
import org.cufy.graphkt.internal.GraphQLObjectTypeBuilderImpl

/* ============= ------------------ ============= */

/**
 * A type for GraphQL objects.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLObjectType<T : Any> : WithName, WithDirectives, GraphQLOutputType<T> {
    /**
     * The fields in this object type.
     *
     * @since 2.0.0
     */
    val fields: List<GraphQLFieldDefinition<T, *>>

    /**
     * The interfaces applied to this.
     *
     * @since 2.0.0
     */
    val interfaces: List<GraphQLInterfaceType<in T>>
}

/**
 * A block of code invoked to fill in options in
 * [GraphQLObjectTypeBuilder].
 */
typealias GraphQLObjectTypeBuilderBlock<T> =
        GraphQLObjectTypeBuilder<T>.() -> Unit

/**
 * A builder for creating a [GraphQLObjectType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLObjectTypeBuilder<T : Any> :
    WithNameBuilder,
    WithFieldsBuilder<T>,
    WithInterfacesBuilder<T>,
    WithDirectivesBuilder,
    WithDeferredBuilder {

    /**
     * Build the type.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLObjectType<T>
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLObjectTypeBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T : Any> GraphQLObjectTypeBuilder(): GraphQLObjectTypeBuilder<T> {
    return GraphQLObjectTypeBuilderImpl()
}

/**
 * Construct a new [GraphQLObjectType] with the
 * given [block].
 *
 * ####
 *
 * When self referencing, you might use `by`:
 *
 * ```
 * val MyObjectType: GraphQLObjectType<T> by GraphQLObjectType {
 *      // ...
 * }
 * ```
 *
 * @param name the initial name.
 * @param block the builder block.
 * @return a new object type.
 * @since 2.0.0
 */
fun <T : Any> GraphQLObjectType(
    name: String? = null,
    block: GraphQLObjectTypeBuilderBlock<T> = {}
): GraphQLObjectType<T> {
    val builder = GraphQLObjectTypeBuilder<T>()
    name?.let { builder.name(it) }
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */
