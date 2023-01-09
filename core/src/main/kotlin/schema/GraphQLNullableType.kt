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

import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.internal.GraphQLInputNullableTypeImpl
import org.cufy.graphkt.internal.GraphQLInputOutputNullableTypeImpl
import org.cufy.graphkt.internal.GraphQLNullableTypeImpl
import org.cufy.graphkt.internal.GraphQLOutputNullableTypeImpl

/* ============= ------------------ ============= */

/**
 * A nullability wrapper for some type.
 *
 * Used to make some graphql type nullable.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLNullableType<T> : GraphQLType<T?> {
    /**
     * The wrapped type.
     *
     * @since 2.0.0
     */
    val type: GraphQLType<T>
}

/**
 * A nullability wrapper for some type.
 *
 * Used to make some graphql type nullable.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputNullableType<T> :
    GraphQLNullableType<T>,
    GraphQLInputType<T?> {
    override val type: GraphQLInputType<T>
}

/**
 * A nullability wrapper for some type.
 *
 * Used to make some graphql type nullable.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLOutputNullableType<T> :
    GraphQLNullableType<T>,
    GraphQLOutputType<T?> {
    override val type: GraphQLOutputType<T>
}

/**
 * A nullability wrapper for some type.
 *
 * Used to make some graphql type nullable.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputOutputNullableType<T> :
    GraphQLInputNullableType<T>,
    GraphQLOutputNullableType<T>,
    GraphQLInputOutputType<T?> {
    override val type: GraphQLInputOutputType<T>
}

/* ============= ------------------ ============= */

/**
 * Wrap the given [type] with a [GraphQLNullableType].
 */
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLNullableType(
    type: GraphQLType<T>
): GraphQLNullableType<T> {
    return GraphQLNullableTypeImpl(type)
}

/**
 * Wrap the given [type] with a [GraphQLInputNullableType].
 */
@Suppress("FunctionName")
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLNullableType(
    type: GraphQLInputType<T>
): GraphQLInputNullableType<T> {
    return GraphQLInputNullableTypeImpl(type)
}

/**
 * Wrap the given [type] with a [GraphQLOutputNullableType].
 */
@Suppress("FunctionName")
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLNullableType(
    type: GraphQLOutputType<T>
): GraphQLOutputNullableType<T> {
    return GraphQLOutputNullableTypeImpl(type)
}

/**
 * Wrap the given [type] with a [GraphQLInputOutputNullableType].
 */
@Suppress("FunctionName")
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLNullableType(
    type: GraphQLInputOutputType<T>
): GraphQLInputOutputNullableType<T> {
    return GraphQLInputOutputNullableTypeImpl(type)
}

/* ============= ------------------ ============= */
