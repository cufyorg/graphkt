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

import kotlin.reflect.KProperty

/**
 * A helper function to bypass kotlin issue with
 * types recursively using themselves.
 *
 * @since 2.0.0
 */
operator fun <T : Any> GraphQLObjectType<T>.getValue(
    nothing: Nothing?,
    property: KProperty<*>
): GraphQLObjectType<T> {
    return this
}

/**
 * A helper function to bypass kotlin issue with
 * types recursively using themselves.
 *
 * @since 2.0.0
 */
operator fun <T : Any> GraphQLInterfaceType<T>.getValue(
    nothing: Nothing?,
    property: KProperty<*>
): GraphQLInterfaceType<T> {
    return this
}

/**
 * A helper function to bypass kotlin issue with
 * types recursively using themselves.
 *
 * @since 2.0.0
 */
operator fun <T : Any> GraphQLInputObjectType<T>.getValue(
    nothing: Nothing?,
    property: KProperty<*>
): GraphQLInputObjectType<T> {
    return this
}

/**
 * Wrap the given [type] with a [GraphQLNullableType].
 */
val <T> GraphQLType<T>.nullable: GraphQLNullableType<T>
    get() = GraphQLNullableType(this)


/**
 * Wrap the given [type] with a [GraphQLInputNullableType].
 */
val <T> GraphQLInputType<T>.nullable: GraphQLInputNullableType<T>
    get() = GraphQLNullableType(this)

/**
 * Wrap the given [type] with a [GraphQLOutputNullableType].
 */
val <T> GraphQLOutputType<T>.nullable: GraphQLOutputNullableType<T>
    get() = GraphQLNullableType(this)

/**
 * Wrap the given [type] with a [GraphQLInputOutputNullableType].
 */
val <T> GraphQLInputOutputType<T>.nullable: GraphQLInputOutputNullableType<T>
    get() = GraphQLNullableType(this)

/**
 * Wrap the given [type] with a [GraphQLArrayType].
 */
val <T> GraphQLType<T>.array: GraphQLArrayType<T>
    get() = GraphQLArrayType(this)

/**
 * Wrap the given [type] with a [GraphQLInputArrayType].
 */
val <T> GraphQLInputType<T>.array: GraphQLInputArrayType<T>
    get() = GraphQLArrayType(this)

/**
 * Wrap the given [type] with a [GraphQLOutputArrayType].
 */
val <T> GraphQLOutputType<T>.array: GraphQLOutputArrayType<T>
    get() = GraphQLArrayType(this)

/**
 * Wrap the given [type] with a [GraphQLInputOutputArrayType].
 */
val <T> GraphQLInputOutputType<T>.array: GraphQLInputOutputArrayType<T>
    get() = GraphQLArrayType(this)
