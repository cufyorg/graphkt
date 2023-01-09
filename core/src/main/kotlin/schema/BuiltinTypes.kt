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

//

/**
 * A scalar type for [Int] and [GraphQLInt].
 *
 * @since 2.0.0
 */
val GraphQLIntType: GraphQLScalarType<Int> = GraphQLScalarType("Int") {
    description { "Built-in Int (32-bit)" }
    decode {
        require(it is GraphQLInt) {
            "Expected GraphQLInt but got ${it.javaClass.simpleName}"
        }
        it.value.toInt()
    }
    encode {
        GraphQLInt(it.toBigInteger())
    }
}

/**
 * A scalar type for [Double] and [GraphQLFloat].
 *
 * @since 2.0.0
 */
val GraphQLFloatType: GraphQLScalarType<Double> = GraphQLScalarType("Float") {
    description { "Built-in Float (64-bit)" }
    decode {
        require(it is GraphQLFloat) {
            "Expected GraphQLFloat but got ${it.javaClass.simpleName}"
        }
        it.value.toDouble()
    }
    encode {
        GraphQLFloat(it.toBigDecimal())
    }
}

/**
 * A scalar type for [String] and [GraphQLString].
 *
 * @since 2.0.0
 */
val GraphQLStringType: GraphQLScalarType<String> = GraphQLScalarType("String") {
    description { "Built-in String" }
    decode {
        require(it is GraphQLString) {
            "Expected GraphQLString but got ${it.javaClass.simpleName}"
        }
        it.value
    }
    encode {
        GraphQLString(it)
    }
}

/**
 * A scalar type for [Boolean] and [GraphQLBoolean].
 *
 * @since 2.0.0
 */
val GraphQLBooleanType: GraphQLScalarType<Boolean> = GraphQLScalarType("Boolean") {
    description { "Built-in Boolean" }
    decode {
        require(it is GraphQLBoolean) {
            "Expected GraphQLBoolean but got ${it.javaClass.simpleName}"
        }
        it.value
    }
    encode {
        GraphQLBoolean(it)
    }
}

/**
 * A scalar type for [String] and [GraphQLString].
 *
 * @since 2.0.0
 */
val GraphQLIDType: GraphQLScalarType<String> = GraphQLScalarType("ID") {
    description { "Built-in ID" }
    decode {
        require(it is GraphQLString) {
            "Expected GraphQLString but got ${it.javaClass.simpleName}"
        }
        it.value
    }
    encode {
        GraphQLString(it)
    }
}

//

/**
 * A scalar type for [Long] and [GraphQLInt].
 *
 * @since 2.0.0
 */
val GraphQLLongType: GraphQLScalarType<Long> = GraphQLScalarType("Long") {
    specifiedBy("https://github.com")
    description { "Long" }
    decode {
        require(it is GraphQLInt) {
            "Expected GraphQLInt but got ${it.javaClass.simpleName}"
        }
        it.value.toLong()
    }
    encode {
        GraphQLInt(it.toBigInteger())
    }
}

/**
 * A scalar type for [BigDecimal] and [GraphQLFloat].
 *
 * @since 2.0.0
 */
val GraphQLDecimalType: GraphQLScalarType<BigDecimal> = GraphQLScalarType("Decimal") {
    description { "Decimal" }
    decode {
        require(it is GraphQLFloat) {
            "Expected GraphQLFloat but got ${it.javaClass.simpleName}"
        }
        it.value
    }
    encode {
        GraphQLFloat(it)
    }
}

/**
 * A scalar type for [BigInteger] and [GraphQLInt].
 *
 * @since 2.0.0
 */
val GraphQLBigIntegerType: GraphQLScalarType<BigInteger> = GraphQLScalarType("Integer") {
    description { "Integer" }
    decode {
        require(it is GraphQLInt) {
            "Expected GraphQLInt but got ${it.javaClass.simpleName}"
        }
        it.value
    }
    encode {
        GraphQLInt(it)
    }
}
