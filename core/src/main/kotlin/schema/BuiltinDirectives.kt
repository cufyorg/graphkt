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

//

@Suppress("PrivatePropertyName")
private val GraphQLDeprecatedReasonArgument = GraphQLArgumentDefinition("reason") {
    type { GraphQLStringType }
    description { "The reason for the deprecation." }
}

/**
 * Marks the field, argument, input field or enum value as deprecated.
 *
 * @since 2.0.0
 */
val GraphQLDeprecatedDirective = GraphQLDirectiveDefinition("deprecated") {
    description { "Marks the field, argument, input field or enum value as deprecated." }
    argument(GraphQLDeprecatedReasonArgument)
    location(GraphQLDirectiveLocation.FIELD_DEFINITION)
    location(GraphQLDirectiveLocation.ENUM_VALUE)
    location(GraphQLDirectiveLocation.ARGUMENT_DEFINITION)
    location(GraphQLDirectiveLocation.INPUT_FIELD_DEFINITION)
}

@Suppress("PrivatePropertyName")
private val GraphQLSpecifiedByUrlArgument = GraphQLArgumentDefinition("url") {
    type { GraphQLStringType }
    description { "The URL that specifies the behaviour of this scalar." }
}

/**
 * Exposes a URL that specifies the behaviour of this scalar.
 *
 * @since 2.0.0
 */
val GraphQLSpecifiedByDirective = GraphQLDirectiveDefinition("specifiedBy") {
    description { "Exposes a URL that specifies the behaviour of this scalar." }
    argument(GraphQLSpecifiedByUrlArgument)
    location(GraphQLDirectiveLocation.SCALAR)
}

/**
 * Directs the executor to include this field or fragment only when the `if` argument is true.
 */
val GraphQLIncludeDirective = GraphQLDirectiveDefinition("include") {
    description { "Directs the executor to include this field or fragment only when the `if` argument is true." }
    argument("if", GraphQLBooleanType) {
        description { "Included when true." }
    }
    location(GraphQLDirectiveLocation.FRAGMENT_SPREAD)
    location(GraphQLDirectiveLocation.INLINE_FRAGMENT)
    location(GraphQLDirectiveLocation.FIELD)
}

/**
 * Directs the executor to skip this field or fragment when the `if`'argument is true.
 */
val GraphQLSkipDirective = GraphQLDirectiveDefinition("skip") {
    description { "Directs the executor to skip this field or fragment when the `if`'argument is true." }
    argument("if", GraphQLBooleanType) {
        description { "Skipped when true." }
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
fun WithDirectivesBuilder.deprecated(reason: String) {
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
fun WithDirectivesBuilder.specifiedBy(url: String) {
    GraphQLSpecifiedByDirective {
        GraphQLSpecifiedByUrlArgument(url)
    }
}
