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
package org.cufy.graphkt.java.internal

import graphql.ExceptionWhileDataFetching
import graphql.ExecutionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.reactive.asFlow
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.*
import org.reactivestreams.Publisher
import graphql.ExecutionInput as JavaExecutionInput
import graphql.ExecutionResult as JavaExecutionResult
import graphql.GraphQLError as JavaGraphQLError
import graphql.language.SourceLocation as JavaSourceLocation
import graphql.schema.GraphQLObjectType as JavaGraphQLObjectType
import graphql.schema.GraphQLSchema as JavaGraphQLSchema

@InternalGraphktApi
fun JavaGraphQLSchema(
    schema: GraphQLSchema
): JavaGraphQLSchema {
    val context = TransformContext()

    //

    val description = schema.description
    val queryType = schema.query?.let { context.addObjectType(it) as JavaGraphQLObjectType }
    val mutationType = schema.mutation?.let { context.addObjectType(it) as JavaGraphQLObjectType }
    val subscriptionType = schema.subscription?.let { context.addObjectType(it) as JavaGraphQLObjectType }
    val additionalTypes = schema.additionalTypes.mapTo(mutableSetOf()) { context.addType(it) }
    val additionalDirectives = schema.additionalDirectives.mapTo(mutableSetOf()) { context.JavaGraphQLDirective(it) }
    val directives = schema.directives.map { context.JavaGraphQLAppliedDirective(it) }

    //

    val codeRegistry = context.codeRegistry.build()

    //

    context.fillRuntime()

    //

    return JavaGraphQLSchema.newSchema()
        .description(description)
        .query(queryType)
        .mutation(mutationType)
        .subscription(subscriptionType)
        .additionalTypes(additionalTypes)
        .additionalDirectives(additionalDirectives)
        .codeRegistry(codeRegistry)
        .withSchemaAppliedDirectives(directives)
        .build()
}

@InternalGraphktApi
fun JavaExecutionInput(
    request: GraphQLRequest,
    context: Map<Any?, Any?>,
    local: Map<Any?, Any?>
): JavaExecutionInput {
    val query = request.query
    val operationName = request.operationName
    val variables = request.variables?.let {
        @Suppress("UNCHECKED_CAST")
        dynamicDecodeFromJsonElement(it)
                as Map<String, Any?>
    } ?: emptyMap()

    return JavaExecutionInput.newExecutionInput()
        .query(query)
        .operationName(operationName)
        .localContext(local)
        .graphQLContext { it.put("graphkt", context) }
        .root(Unit)
        .variables(variables)
        .build()
}

@Suppress("FunctionName")
@InternalGraphktApi
fun GraphQLResponse(
    result: JavaExecutionResult
): Flow<GraphQLResponse> {
    val sourceData = result.getData<Any>() ?: null

    if (sourceData is Publisher<*>) {
        return sourceData.asFlow().map {
            it as ExecutionResult
            GraphQLResponse(it).single()
        }
    }

    val data = dynamicEncodeToJsonElement(sourceData) as? JsonObject
    val errors = result.errors.map { GraphQLError(it) }
    val extensions = dynamicEncodeToJsonElement(result.extensions) as? JsonObject

    return flowOf(
        GraphQLResponse(
            errors = errors,
            data = data,
            extensions = extensions
        )
    )
}

@InternalGraphktApi
fun GraphQLError(
    error: JavaGraphQLError
): GraphQLError {
    val message = error.message
    val locations = error.locations.map { GraphQLErrorLocation(it) }
    val path = error.path?.map { if (it is Number) JsonPrimitive(it) else JsonPrimitive("$it") }
    val extensions = dynamicEncodeToJsonElement(error.extensions) as? JsonObject
    val cause = when (error) {
        is ExceptionWhileDataFetching -> error.exception
        else -> null
    }

    return GraphQLError(
        message = message,
        locations = locations,
        path = path,
        extensions = extensions,
        cause = cause
    )
}

@InternalGraphktApi
fun GraphQLErrorLocation(
    location: JavaSourceLocation
): GraphQLErrorLocation {
    val line = location.line
    val column = location.column

    return GraphQLErrorLocation(
        line = line,
        column = column
    )
}
