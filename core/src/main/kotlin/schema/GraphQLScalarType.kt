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
import org.cufy.graphkt.internal.GraphQLScalarTypeBuilderImpl

/* ============= ------------------ ============= */

/**
 * An interface for scalar types.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLScalarType<T : Any> : WithName, WithDirectives, GraphQLInputOutputType<T> {
    /**
     * Decode the given graphql [value] into a value of type [T]
     */
    fun decode(value: GraphQLScalar<*>): T

    /**
     * Encode the given [value] into a graphql value.
     */
    fun encode(value: T): GraphQLScalar<*>
}

/**
 * The scalar encoder function.
 */
typealias GraphQLScalarDecoder<T> = (GraphQLScalar<*>) -> T

/**
 * The scalar decoder function.
 */
typealias GraphQLScalarEncoder<T> = (T) -> GraphQLScalar<*>

/**
 * A block of code invoked to fill in options in
 * [GraphQLScalarTypeBuilder].
 */
typealias GraphQLScalarTypeBuilderBlock<T> =
        GraphQLScalarTypeBuilder<T>.() -> Unit

/**
 * A builder for creating a [GraphQLScalarType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLScalarTypeBuilder<T : Any> :
    WithNameBuilder,
    WithDirectivesBuilder,
    WithDeferredBuilder {

    /**
     * The scalar encoder.
     */
    @AdvancedGraphktApi("Use `encode()` instead")
    var encoder: GraphQLScalarEncoder<T>? // REQUIRED

    /**
     * The scalar decoder.
     */
    @AdvancedGraphktApi("Use `decode()` instead")
    var decoder: GraphQLScalarDecoder<T>? // REQUIRED

    /**
     * Build the type.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLScalarType<T>
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLScalarTypeBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T : Any> GraphQLScalarTypeBuilder(): GraphQLScalarTypeBuilder<T> {
    return GraphQLScalarTypeBuilderImpl()
}

/**
 * Construct a new [GraphQLScalarType] with the
 * given [block].
 *
 * @param name the initial name.
 * @param block the builder block.
 * @return a new scalar type.
 * @since 2.0.0
 */
fun <T : Any> GraphQLScalarType(
    name: String? = null,
    block: GraphQLScalarTypeBuilderBlock<T> = {}
): GraphQLScalarType<T> {
    val builder = GraphQLScalarTypeBuilder<T>()
    name?.let { builder.name(it) }
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */

// encoder

/**
 * Set the encoder to the given [encoder].
 *
 * This will replace the current encoder.
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any> GraphQLScalarTypeBuilder<T>.encode(
    encoder: GraphQLScalarEncoder<T>
) {
    this.encoder = encoder
}

// decoder

/**
 * Set the decoder to the given [decoder].
 *
 * This will replace the current decoder.
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any> GraphQLScalarTypeBuilder<T>.decode(
    decoder: GraphQLScalarDecoder<T>
) {
    this.decoder = decoder
}

/* ============= ------------------ ============= */
