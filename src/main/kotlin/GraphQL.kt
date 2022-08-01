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
package org.cufy.kaguya

import graphql.ErrorType
import graphql.GraphQL
import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import graphql.schema.GraphQLSchema
import java.util.concurrent.CompletionException

/**
 * A kotlin-friendly wrapper over [GraphQL.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLBuilder(schema: GraphQLSchema) :
    GraphQL.Builder(schema)

// Constructors

/**
 * Create a new [GraphQL] and apply the given
 * [block] to it.
 *
 * @since 1.0.0
 */
inline fun GraphQL(
    schema: GraphQLSchema,
    block: GraphQLBuilder.() -> Unit = {}
): GraphQL {
    val builder = GraphQLBuilder(schema)
    builder.apply(block)
    return builder.build()
}

// Extensions

/**
 * Override the default exception handler.
 *
 * @param block a function to be applied when
 *              constructing a graphql error for
 *              a handled exception.
 * @since 1.0.0
 */
fun GraphQLBuilder.onException(
    block: GraphqlErrorExceptionBuilder.(
        DataFetcherExceptionHandlerParameters
    ) -> Unit = {}
) {
    defaultDataFetcherExceptionHandler(object : DataFetcherExceptionHandler {
        @Suppress("OVERRIDE_DEPRECATION")
        override fun onException(
            parameters: DataFetcherExceptionHandlerParameters
        ): DataFetcherExceptionHandlerResult {
            val exception = parameters.exception.cause
                ?.takeIf { parameters.exception is CompletionException }
                ?: parameters.exception

            val error = GraphqlErrorException {
                message = exception.message
                sourceLocations = listOf(parameters.sourceLocation)
                path = parameters.path.toList()
                extensions = exception.let { it as? GraphQLError }?.extensions
                errorClassification = ErrorType.DataFetchingException
                block(parameters)
            }

            return DataFetcherExceptionHandlerResult
                .newResult()
                .error(error)
                .build()
        }
    })
}
