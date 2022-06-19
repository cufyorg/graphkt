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
package org.cufy.kaguya.graphql

import graphql.language.IntValue
import graphql.language.Value
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import java.math.BigInteger

open class GraphqlLongCoercing : Coercing<Long, Long> {
    companion object {
        private val LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE)
        private val LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE)
    }

    private fun convertImpl(input: Any): Long? {
        return when (input) {
            is Long -> input
            is Number -> input.toLong()
            is String -> input.toLongOrNull()
            else -> null
        }
    }

    override fun serialize(input: Any): Long {
        @Suppress("UNNECESSARY_SAFE_CALL")
        return convertImpl(input)
            ?: throw CoercingSerializeException(
                "Expected type 'Long' but was '${input?.javaClass?.simpleName}'."
            )
    }

    override fun parseValue(input: Any): Long {
        @Suppress("UNNECESSARY_SAFE_CALL")
        return convertImpl(input)
            ?: throw CoercingSerializeException(
                "Expected type 'Long' but was '${input?.javaClass?.simpleName}'."
            )
    }

    override fun parseLiteral(input: Any): Long {
        @Suppress("UNNECESSARY_SAFE_CALL")
        input as? IntValue
            ?: throw CoercingParseLiteralException(
                "Expected AST type 'IntValue' but was '${input?.javaClass?.simpleName}'."
            )
        val value: BigInteger = input.value
        if (value < LONG_MIN || value > LONG_MAX)
            throw CoercingParseLiteralException(
                "Expected value to be in the Long range but it was '$value'"
            )
        return value.longValueExact()
    }

    override fun valueToLiteral(input: Any): Value<out Value<*>> {
        val result = convertImpl(input) ?: error("Value cannot be serialized to Long: $input")
        return IntValue.newIntValue(BigInteger.valueOf(result)).build()
    }
}

object ExtraScalars {
    val GraphQLLong: GraphQLScalarType =
        GraphQLScalarType.newScalar()
            .name("Long")
            .description("Built-in Long")
            .coercing(GraphqlLongCoercing())
            .build()
}
