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
import org.cufy.graphkt.internal.GraphQLInputObjectTypeBuilderImpl

/* ============= ------------------ ============= */

/**
 * A type for GraphQL input objects.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputObjectType<T : Any> : WithName, WithDirectives, GraphQLInputType<T> {
    /**
     * The fields in this input object type.
     *
     * @since 2.0.0
     */
    val fields: List<GraphQLInputFieldDefinition<T, *>>

    /**
     * The constructor of the runtime value [T].
     */
    val constructor: GraphQLInputConstructor<T>
}

/**
 * An input object constructor.
 *
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLInputConstructor<T> =
            () -> T

/**
 * A block of code invoked to fill in options in
 * [GraphQLInputObjectTypeBuilder].
 */
typealias GraphQLInputObjectTypeBuilderBlock<T> =
        GraphQLInputObjectTypeBuilder<T>.() -> Unit

/**
 * A builder for creating a [GraphQLInputObjectType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputObjectTypeBuilder<T : Any> :
    WithNameBuilder,
    WithInputFieldsBuilder<T>,
    WithDirectivesBuilder,
    WithDeferredBuilder {

    /**
     * The constructor of type [T].
     */
    @AdvancedGraphktApi("Use `construct()` instead")
    var constructor: GraphQLInputConstructor<T>? // REQUIRED

    /**
     * Build the type.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLInputObjectType<T>
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLInputObjectTypeBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T : Any> GraphQLInputObjectTypeBuilder(): GraphQLInputObjectTypeBuilder<T> {
    return GraphQLInputObjectTypeBuilderImpl()
}

/**
 * Construct a new [GraphQLInputObjectType] with the
 * given [block].
 *
 * @param name the initial name.
 * @param constructor the initial constructor.
 * @param block the builder block.
 * @return a new object type.
 * @since 2.0.0
 */
fun <T : Any> GraphQLInputObjectType(
    name: String? = null,
    constructor: GraphQLInputConstructor<T>? = null,
    block: GraphQLInputObjectTypeBuilderBlock<T>
): GraphQLInputObjectType<T> {
    val builder = GraphQLInputObjectTypeBuilder<T>()
    name?.let { builder.name(it) }
    constructor?.let { builder.construct(it) }
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */

/**
 * Set the constructor to the given [constructor].
 *
 * This will replace the current constructor.
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any> GraphQLInputObjectTypeBuilder<T>.construct(
    constructor: GraphQLInputConstructor<T>
) {
    this.constructor = constructor
}

/* ============= ------------------ ============= */
