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
import org.cufy.graphkt.internal.GraphQLInputOutputArrayTypeImpl
import org.cufy.graphkt.internal.GraphQLInputArrayTypeImpl
import org.cufy.graphkt.internal.GraphQLOutputArrayTypeImpl
import org.cufy.graphkt.internal.GraphQLArrayTypeImpl

/* ============= ------------------ ============= */

/**
 * An array wrapper for some type.
 *
 * Used to make an array type of some graphql type.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 0.2.0
 */
interface GraphQLArrayType<T> : GraphQLType<List<T>> {
    /**
     * The type of the items in the array.
     *
     * @since 2.0.0
     */
    val type: GraphQLType<T>
}

/**
 * An array wrapper for some type.
 *
 * Used to make an array type of some graphql type.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 0.2.0
 */
interface GraphQLInputArrayType<T> :
    GraphQLArrayType<T>,
    GraphQLInputType<List<T>> {
    override val type: GraphQLInputType<T>
}

/**
 * An array wrapper for some type.
 *
 * Used to make an array type of some graphql type.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 0.2.0
 */
interface GraphQLOutputArrayType<T> :
    GraphQLArrayType<T>,
    GraphQLOutputType<List<T>> {
    override val type: GraphQLOutputType<T>
}

/**
 * An array wrapper for some type.
 *
 * Used to make an array type of some graphql type.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 0.2.0
 */
interface GraphQLInputOutputArrayType<T> :
    GraphQLInputArrayType<T>,
    GraphQLOutputArrayType<T>,
    GraphQLInputOutputType<List<T>> {
    override val type: GraphQLInputOutputType<T>
}

/* ============= ------------------ ============= */

/**
 * Wrap the given [type] with a [GraphQLArrayType].
 */
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLArrayType(
    type: GraphQLType<T>
): GraphQLArrayType<T> {
    return GraphQLArrayTypeImpl(type)
}

/**
 * Wrap the given [type] with a [GraphQLInputArrayType].
 */
@Suppress("FunctionName")
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLArrayType(
    type: GraphQLInputType<T>
): GraphQLInputArrayType<T> {
    return GraphQLInputArrayTypeImpl(type)
}

/**
 * Wrap the given [type] with a [GraphQLOutputArrayType].
 */
@Suppress("FunctionName")
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLArrayType(
    type: GraphQLOutputType<T>
): GraphQLOutputArrayType<T> {
    return GraphQLOutputArrayTypeImpl(type)
}

/**
 * Wrap the given [type] with a [GraphQLInputOutputArrayType].
 */
@Suppress("FunctionName")
@OptIn(InternalGraphktApi::class)
fun <T> GraphQLArrayType(
    type: GraphQLInputOutputType<T>
): GraphQLInputOutputArrayType<T> {
    return GraphQLInputOutputArrayTypeImpl(type)
}

/* ============= ------------------ ============= */
