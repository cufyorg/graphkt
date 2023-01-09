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
package org.cufy.graphkt.internal

import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.*

@InternalGraphktApi
open class GraphQLArrayTypeImpl<T>(
    override val type: GraphQLType<T>
) : GraphQLArrayType<T> {
    override fun toString(): String = "GraphQLArrayType($type)"
}

@InternalGraphktApi
open class GraphQLInputArrayTypeImpl<T>(
    override val type: GraphQLInputType<T>
) : GraphQLInputArrayType<T> {
    override fun toString(): String = "GraphQLArrayType($type)"
}

@InternalGraphktApi
open class GraphQLOutputArrayTypeImpl<T>(
    override val type: GraphQLOutputType<T>
) : GraphQLOutputArrayType<T> {
    override fun toString(): String = "GraphQLArrayType($type)"
}

@InternalGraphktApi
open class GraphQLInputOutputArrayTypeImpl<T>(
    override val type: GraphQLInputOutputType<T>
) : GraphQLInputOutputArrayType<T> {
    override fun toString(): String = "GraphQLArrayType($type)"
}

//

@InternalGraphktApi
open class GraphQLNullableTypeImpl<T>(
    override val type: GraphQLType<T>
) : GraphQLNullableType<T> {
    override fun toString(): String = "GraphQLNullableType($type)"
}

@InternalGraphktApi
open class GraphQLInputNullableTypeImpl<T>(
    override val type: GraphQLInputType<T>
) : GraphQLInputNullableType<T> {
    override fun toString(): String = "GraphQLNullableType($type)"
}

@InternalGraphktApi
open class GraphQLOutputNullableTypeImpl<T>(
    override val type: GraphQLOutputType<T>
) : GraphQLOutputNullableType<T> {
    override fun toString(): String = "GraphQLNullableType($type)"
}

@InternalGraphktApi
open class GraphQLInputOutputNullableTypeImpl<T>(
    override val type: GraphQLInputOutputType<T>
) : GraphQLInputOutputNullableType<T> {
    override fun toString(): String = "GraphQLNullableType($type)"
}
