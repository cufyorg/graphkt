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

private val GraphQLDeprecatedReasonArgument = GraphQLArgumentDefinition("reason") {
    description = "The reason for the deprecation."
    type { GraphQLStringType }
}

/**
 * Marks the field, argument, input field or enum value as deprecated.
 *
 * @since 2.0.0
 */
val GraphQLDeprecatedDirective = GraphQLDirectiveDefinition("deprecated") {
    description = "Marks the field, argument, input field or enum value as deprecated."
    argument(GraphQLDeprecatedReasonArgument)
    location(GraphQLDirectiveLocation.FIELD_DEFINITION)
    location(GraphQLDirectiveLocation.ENUM_VALUE)
    location(GraphQLDirectiveLocation.ARGUMENT_DEFINITION)
    location(GraphQLDirectiveLocation.INPUT_FIELD_DEFINITION)
}

private val GraphQLSpecifiedByUrlArgument = GraphQLArgumentDefinition("url") {
    description = "The URL that specifies the behaviour of this scalar."
    type { GraphQLStringType }
}

/**
 * Exposes a URL that specifies the behaviour of this scalar.
 *
 * @since 2.0.0
 */
val GraphQLSpecifiedByDirective = GraphQLDirectiveDefinition("specifiedBy") {
    description = "Exposes a URL that specifies the behaviour of this scalar."
    argument(GraphQLSpecifiedByUrlArgument)
    location(GraphQLDirectiveLocation.SCALAR)
}

/**
 * Directs the executor to include this field or fragment only when the `if` argument is true.
 */
val GraphQLIncludeDirective = GraphQLDirectiveDefinition("include") {
    description = "Directs the executor to include this field or fragment only when the `if` argument is true."
    argument("if") {
        description = "Included when true."
        type { GraphQLBooleanType }
    }
    location(GraphQLDirectiveLocation.FRAGMENT_SPREAD)
    location(GraphQLDirectiveLocation.INLINE_FRAGMENT)
    location(GraphQLDirectiveLocation.FIELD)
}

/**
 * Directs the executor to skip this field or fragment when the `if`'argument is true.
 */
val GraphQLSkipDirective = GraphQLDirectiveDefinition("skip") {
    description = "Directs the executor to skip this field or fragment when the `if`'argument is true."
    argument("if") {
        description = "Skipped when true."
        type { GraphQLBooleanType }
    }
    location(GraphQLDirectiveLocation.FRAGMENT_SPREAD)
    location(GraphQLDirectiveLocation.INLINE_FRAGMENT)
    location(GraphQLDirectiveLocation.FIELD)
}

//

/**
 * Marks the field, argument, input field or enum value as deprecated.
 *
 * @param reason the reason for the deprecation.
 * @since 2.0.0
 */
fun GraphQLMutableElementWithDirectives.deprecated(reason: String) {
    GraphQLDeprecatedDirective {
        GraphQLDeprecatedReasonArgument(reason)
    }
}

/**
 * Exposes a URL that specifies the behaviour of this scalar.
 *
 * @param url the URL that specifies the behaviour of this scalar.
 * @since 2.0.0
 */
fun GraphQLMutableElementWithDirectives.specifiedBy(url: String) {
    GraphQLSpecifiedByDirective {
        GraphQLSpecifiedByUrlArgument(url)
    }
}

//

/**
 * A scalar type for [Int] and [GraphQLInteger].
 *
 * @since 2.0.0
 */
val GraphQLIntType: GraphQLScalarType<Int> = GraphQLScalarType("Int") {
    description = "Built-in Int (32-bit)"
    decode {
        require(it is GraphQLInteger) {
            "Expected GraphQLInteger but got ${it.javaClass.simpleName}"
        }
        it.value.toInt()
    }
    encode {
        GraphQLInteger(it.toBigInteger())
    }
}

/**
 * A scalar type for [Double] and [GraphQLDecimal].
 *
 * @since 2.0.0
 */
val GraphQLFloatType: GraphQLScalarType<Double> = GraphQLScalarType("Float") {
    description = "Built-in Float (64-bit)"
    decode {
        when (it) {
            is GraphQLDecimal -> it.value.toDouble()
            is GraphQLInteger -> it.value.toDouble()
            else -> throw IllegalArgumentException(
                "Expected GraphQLDecimal but got ${it.javaClass.simpleName}"
            )
        }
    }
    encode {
        GraphQLDecimal(it.toBigDecimal())
    }
}


/**
 * A scalar type for [String] and [GraphQLString].
 *
 * @since 2.0.0
 */
val GraphQLStringType: GraphQLScalarType<String> = GraphQLScalarType("String") {
    description = "Built-in String"
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
    description = "Built-in Boolean"
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
    description = "Built-in ID"
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
 * A non-standard scalar type for [Long] and [GraphQLInteger].
 *
 * @since 2.0.0
 */
val GraphQLLongType: GraphQLScalarType<Long> = GraphQLScalarType("Long") {
    description = "Long"
    specifiedBy("https://github.com")
    decode {
        require(it is GraphQLInteger) {
            "Expected GraphQLInteger but got ${it.javaClass.simpleName}"
        }
        it.value.toLong()
    }
    encode {
        GraphQLInteger(it.toBigInteger())
    }
}

/**
 * A non-standard scalar type for [BigDecimal] and [GraphQLDecimal].
 *
 * @since 2.0.0
 */
val GraphQLDecimalType: GraphQLScalarType<BigDecimal> = GraphQLScalarType("Decimal") {
    description = "Decimal"
    decode {
        when (it) {
            is GraphQLDecimal -> it.value
            is GraphQLInteger -> it.value.toBigDecimal()
            else -> throw IllegalArgumentException(
                "Expected GraphQLDecimal but got ${it.javaClass.simpleName}"
            )
        }
    }
    encode {
        GraphQLDecimal(it)
    }
}

/**
 * A non-standard scalar type for [BigInteger] and [GraphQLInteger].
 *
 * @since 2.0.0
 */
val GraphQLIntegerType: GraphQLScalarType<BigInteger> = GraphQLScalarType("Integer") {
    description = "Integer"
    decode {
        require(it is GraphQLInteger) {
            "Expected GraphQLInteger but got ${it.javaClass.simpleName}"
        }
        it.value
    }
    encode {
        GraphQLInteger(it)
    }
}

/**
 * A non-standard scalar type that returns nothing. (like [Unit] or [Void])
 *
 * @since 2.0.0
 */
val GraphQLVoidType = GraphQLScalarType("Void") {
    description = "Void"
    decode { }
    encode { GraphQLBoolean(true) }
}

//
