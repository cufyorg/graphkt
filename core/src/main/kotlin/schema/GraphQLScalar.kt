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

import java.math.BigDecimal
import java.math.BigInteger

/**
 * The type of a graphql scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLScalar<V> {
    /**
     * The scalar's runtime value.
     */
    val value: V
}

/**
 * The graphql string scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLString(
    override val value: String
) : GraphQLScalar<String>

/**
 * The graphql boolean scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLBoolean(
    override val value: Boolean
) : GraphQLScalar<Boolean>

/**
 * The graphql int scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLInt(
    override val value: BigInteger
) : GraphQLScalar<BigInteger>

/**
 * The graphql float scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLFloat(
    override val value: BigDecimal
) : GraphQLScalar<BigDecimal>
