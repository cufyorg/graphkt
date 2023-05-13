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

import graphql.language.OperationDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asPublisher
import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.*
import java.util.concurrent.CompletableFuture
import kotlin.streams.asSequence
import graphql.TypeResolutionEnvironment as JavaTypeResolutionEnvironment
import graphql.execution.DataFetcherResult as JavaDataFetcherResult
import graphql.execution.directives.QueryAppliedDirective as JavaQueryAppliedDirective
import graphql.execution.directives.QueryAppliedDirectiveArgument as JavaQueryAppliedDirectiveArgument
import graphql.introspection.Introspection.DirectiveLocation as JavaDirectiveLocation
import graphql.language.BooleanValue as JavaBooleanValue
import graphql.language.FloatValue as JavaFloatValue
import graphql.language.IntValue as JavaIntValue
import graphql.language.StringValue as JavaStringValue
import graphql.language.Value as JavaValue
import graphql.schema.Coercing as JavaCoercing
import graphql.schema.CoercingParseLiteralException as JavaCoercingParseLiteralException
import graphql.schema.CoercingParseValueException as JavaCoercingParseValueException
import graphql.schema.CoercingSerializeException as JavaCoercingSerializeException
import graphql.schema.DataFetcher as JavaDataFetcher
import graphql.schema.DataFetchingEnvironment as JavaDataFetchingEnvironment
import graphql.schema.GraphQLAppliedDirective as JavaGraphQLAppliedDirective
import graphql.schema.GraphQLAppliedDirectiveArgument as JavaGraphQLAppliedDirectiveArgument
import graphql.schema.TypeResolver as JavaTypeResolver

/*==================================================
================ Pure Constructors  ================
==================================================*/

/* ============= ----- Scalar ----- ============= */

@InternalGraphktApi
fun transformToGraphQLScalar(value: JavaValue<*>): GraphQLScalar<*> {
    return when (value) {
        is JavaIntValue -> GraphQLInteger(value.value)
        is JavaFloatValue -> GraphQLDecimal(value.value)
        is JavaStringValue -> GraphQLString(value.value)
        is JavaBooleanValue -> GraphQLBoolean(value.isValue)
        else -> error("Unexpected Java Value: $value")
    }
}

@InternalGraphktApi
fun transformGraphQLScalar(scalar: GraphQLScalar<*>): JavaValue<*> {
    return when (scalar) {
        is GraphQLInteger -> JavaIntValue.newIntValue(scalar.value).build()
        is GraphQLDecimal -> JavaFloatValue.newFloatValue(scalar.value).build()
        is GraphQLString -> JavaStringValue.newStringValue(scalar.value).build()
        is GraphQLBoolean -> JavaBooleanValue.newBooleanValue(scalar.value).build()
        else -> error("Unexpected GraphQL Value: $scalar")
    }
}

/* ============= -- JavaCoercing -- ============= */

@InternalGraphktApi
fun <T : Any> createJavaCoercing(type: GraphQLScalarType<T>): JavaCoercing<T, GraphQLScalar<*>> {
    // TODO: testing
    return object : JavaCoercing<T, GraphQLScalar<*>> {
        override fun serialize(dataFetcherResult: Any): GraphQLScalar<*> {
            // resolvers will always return the correct type: T
            @Suppress("UNCHECKED_CAST")
            val result = dataFetcherResult as T

            try {
                return type.encoder(result)
            } catch (error: JavaCoercingSerializeException) {
                throw error
            } catch (error: Throwable) {
                throw JavaCoercingSerializeException(error)
            }
        }

        override fun parseLiteral(input: Any): T {
            input as JavaValue<*>

            try {
                return type.decoder(transformToGraphQLScalar(input))
            } catch (error: JavaCoercingParseLiteralException) {
                throw error
            } catch (error: Throwable) {
                throw JavaCoercingParseLiteralException(error)
            }
        }

        override fun parseValue(input: Any): T {
            val scalar = dynamicDecodeScalar(input)

            try {
                return type.decoder(scalar)
            } catch (error: JavaCoercingParseValueException) {
                throw error
            } catch (error: Throwable) {
                throw JavaCoercingParseValueException(error)
            }
        }

        override fun valueToLiteral(input: Any): JavaValue<out JavaValue<*>> {
            // variables will always be of type GraphQLScalar
            input as GraphQLScalar<*>

            // invoking this was just to comply with the specs
            type.decoder(input)
            return transformGraphQLScalar(input)
        }
    }
}

/* =========== - DirectiveLocation  - =========== */

@InternalGraphktApi
fun transformGraphQLDirectiveLocation(
    location: GraphQLDirectiveLocation
): JavaDirectiveLocation {
    return when (location) {
        GraphQLDirectiveLocation.QUERY ->
            JavaDirectiveLocation.QUERY
        GraphQLDirectiveLocation.MUTATION ->
            JavaDirectiveLocation.MUTATION
        GraphQLDirectiveLocation.SUBSCRIPTION ->
            JavaDirectiveLocation.SUBSCRIPTION
        GraphQLDirectiveLocation.FIELD ->
            JavaDirectiveLocation.FIELD
        GraphQLDirectiveLocation.FRAGMENT_DEFINITION ->
            JavaDirectiveLocation.FRAGMENT_DEFINITION
        GraphQLDirectiveLocation.FRAGMENT_SPREAD ->
            JavaDirectiveLocation.FRAGMENT_SPREAD
        GraphQLDirectiveLocation.INLINE_FRAGMENT ->
            JavaDirectiveLocation.INLINE_FRAGMENT
        GraphQLDirectiveLocation.VARIABLE_DEFINITION ->
            JavaDirectiveLocation.VARIABLE_DEFINITION
        //
        GraphQLDirectiveLocation.SCHEMA ->
            JavaDirectiveLocation.SCHEMA
        GraphQLDirectiveLocation.SCALAR ->
            JavaDirectiveLocation.SCALAR
        GraphQLDirectiveLocation.OBJECT ->
            JavaDirectiveLocation.OBJECT
        GraphQLDirectiveLocation.FIELD_DEFINITION ->
            JavaDirectiveLocation.FIELD_DEFINITION
        GraphQLDirectiveLocation.ARGUMENT_DEFINITION ->
            JavaDirectiveLocation.ARGUMENT_DEFINITION
        GraphQLDirectiveLocation.INTERFACE ->
            JavaDirectiveLocation.INTERFACE
        GraphQLDirectiveLocation.UNION ->
            JavaDirectiveLocation.UNION
        GraphQLDirectiveLocation.ENUM ->
            JavaDirectiveLocation.ENUM
        GraphQLDirectiveLocation.ENUM_VALUE ->
            JavaDirectiveLocation.ENUM_VALUE
        GraphQLDirectiveLocation.INPUT_OBJECT ->
            JavaDirectiveLocation.INPUT_OBJECT
        GraphQLDirectiveLocation.INPUT_FIELD_DEFINITION ->
            JavaDirectiveLocation.INPUT_FIELD_DEFINITION
    }
}

/* ============= ---- Argument ---- ============= */

@InternalGraphktApi
fun <T> createGraphQLArgument(
    argument: JavaQueryAppliedDirectiveArgument,
    definition: GraphQLArgumentDefinition<T>
): GraphQLArgument<T> {
    val rawValue = argument.getValue<Any?>()
    val value = definition.type.value.furtherDecodeArgumentValue(rawValue)

    return GraphQLArgument(
        definition,
        value
    )
}

@InternalGraphktApi
fun <T> createGraphQLArgument(
    rawValue: Any?,
    definition: GraphQLArgumentDefinition<T>
): GraphQLArgument<T> {
    val value = definition.type.value.furtherDecodeArgumentValue(rawValue)

    return GraphQLArgument(
        definition,
        value
    )
}

/*==================================================
============= Build Time Constructors  =============
==================================================*/

/* ============= ---- Argument ---- ============= */

@InternalGraphktApi
fun <T> TransformContext.transformGraphQLArgument(
    argument: GraphQLArgument<T>
): JavaGraphQLAppliedDirectiveArgument {
    val definition = transformGraphQLArgumentDefinition(argument.definition)
    val rawValue = argument.definition.type.value.furtherEncodeArgumentValue(argument.value)

    return JavaGraphQLAppliedDirectiveArgument
        .newArgument()
        .name(definition.name)
        .description(definition.description)
        .type(definition.type)
        .valueProgrammatic(rawValue)
        .build()
}

/* ============= --- Directive  --- ============= */

@InternalGraphktApi
fun TransformContext.transformGraphQLDirective(
    directive: GraphQLDirective
): JavaGraphQLAppliedDirective {
    addDirectiveDefinition(directive.definition)

    val name = directive.definition.name
    val description = directive.definition.name
    val arguments = directive.arguments.map {
        transformGraphQLArgument(it)
    }

    return JavaGraphQLAppliedDirective
        .newDirective()
        .name(name)
        .description(description)
        .replaceArguments(arguments)
        .build()
}

/*==================================================
=============== Runtime Constructors ===============
==================================================*/

/* ============= --- Directive  --- ============= */

@InternalGraphktApi
fun TransformRuntimeContext.transformToGraphQLDirective(
    directive: JavaQueryAppliedDirective
): GraphQLDirective {
    val name = directive.name
    val definition = getDirective(name)
    val arguments = directive.arguments.map { argument ->
        val argumentDefinition = definition.arguments.first { it.name == argument.name }

        createGraphQLArgument(argument, argumentDefinition)
    }

    return GraphQLDirective(
        definition,
        arguments
    )
}

/* ============= ----- Getter ----- ============= */

@InternalGraphktApi
fun <T : Any, M> TransformRuntimeContext.createJavaDataFetcher(
    getter: GraphQLFlowGetter<T, M>,
    onGetBlocks: List<GraphQLGetterBlock<T, M>>,
    onGetBlockingBlocks: List<GraphQLGetterBlockingBlock<T, M>>,
    definition: GraphQLFieldDefinition<T, M>,
): JavaDataFetcher<*> {
    return JavaDataFetcher { environment ->
        val scope = createGraphQLGetterScope(environment, definition)

        onGetBlockingBlocks.forEach {
            it(scope)
        }

        val future = CompletableFuture<Any?>()

        CoroutineScope(Dispatchers.Default).launch {
            val flow = try {
                onGetBlocks.forEach {
                    it(scope)
                }

                getter(scope).map {
                    createJavaDataFetcherResult(it, scope.supLocal + scope.subLocal)
                }
            } catch (error: Throwable) {
                future.completeExceptionally(error)
                return@launch
            }

            /*
            The driver expects a CompletableFuture with
            the actual value for Queries and Mutations
            (and non-root Subscription fields)

            And a CompletableFuture with a Publisher of
            the value for Subscriptions
            */
            if (environment.canReturnPublisher())
                future.complete(flow.asPublisher())
            else
                future.complete(flow.singleOrNull() ?: error(
                    "None Subscription root fields are expected to return a single value."
                ))
        }

        future
    }
}

private fun JavaDataFetchingEnvironment.canReturnPublisher(): Boolean {
    return operationDefinition.operation == OperationDefinition.Operation.SUBSCRIPTION &&
            executionStepInfo.path.parent.isRootPath
}

private fun <T> createJavaDataFetcherResult(
    data: T,
    local: Map<Any?, Any?>
): JavaDataFetcherResult<T> {
    return JavaDataFetcherResult.newResult<T>()
        .data(data)
        .localContext(local)
        .build()
}

@InternalGraphktApi
private fun <T : Any, M> TransformRuntimeContext.createGraphQLGetterScope(
    environment: JavaDataFetchingEnvironment,
    definition: GraphQLFieldDefinition<T, M>,
): GraphQLGetterScope<T, M> {
    val instance = environment.getSource<T>()
    val arguments = definition.arguments.map {
        createGraphQLArgument(rawValue = environment.getArgument(it.name), it)
    }
    val directives = environment
        .queryDirectives
        .immediateAppliedDirectivesByName
        .values
        .flatten()
        .map { transformToGraphQLDirective(it) }
    val graphQlContext = environment
        .graphQlContext
        .get<Map<Any?, Any?>>("graphkt")
            ?: emptyMap()
    val supLocal = environment
        .getLocalContext() as? Map<Any?, Any?>
            ?: emptyMap()
    val subLocal = mutableMapOf<Any?, Any?>()

    @Suppress("UNCHECKED_CAST")
    return GraphQLGetterScopeImpl(
        instance ?: Unit as T,
        arguments,
        directives,
        graphQlContext,
        supLocal,
        subLocal
    )
}

@InternalGraphktApi
open class GraphQLGetterScopeImpl<T : Any, M>(
    override val instance: T,
    override val arguments: List<GraphQLArgument<*>>,
    override val directives: List<GraphQLDirective>,
    override val context: Map<Any?, Any?>,
    override val supLocal: Map<Any?, Any?>,
    override val subLocal: MutableMap<Any?, Any?>
) : GraphQLGetterScope<T, M> {
    override val local: MutableMap<Any?, Any?> = mutableMapOf()
}

/* ============= --- TypeGetter --- ============= */

@InternalGraphktApi
fun <T : Any> TransformRuntimeContext.createJavaTypeResolver(
    getter: GraphQLTypeGetter<T>
): JavaTypeResolver {
    return JavaTypeResolver { environment ->
        val scope = createGraphQLTypeGetterScope<T>(environment)
        val data = getter(scope)

        val type = getObjectType(data)

        type
    }
}

@Suppress("UnusedReceiverParameter")
@InternalGraphktApi
private fun <T : Any> TransformRuntimeContext.createGraphQLTypeGetterScope(
    environment: JavaTypeResolutionEnvironment
): GraphQLTypeGetterScope<T> {
    val instance = environment.getObject<T>()
    val graphQlContext = environment
        .graphQLContext
        .stream()
        .asSequence()
        .associate { it.key to it.value }

    return GraphQLTypeGetterScopeImpl(
        instance,
        graphQlContext
    )
}

@InternalGraphktApi
open class GraphQLTypeGetterScopeImpl<T : Any>(
    override val instance: T,
    override val context: Map<Any?, Any?>
) : GraphQLTypeGetterScope<T>
