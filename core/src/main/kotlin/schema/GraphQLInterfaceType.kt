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

import org.cufy.graphkt.*
import org.cufy.graphkt.internal.GraphQLInterfaceTypeBuilderImpl
import org.cufy.graphkt.internal.GraphQLTypeGetterScopeImpl

/* ============= ------------------ ============= */

/**
 * A type for GraphQL interfaces.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInterfaceType<T : Any> : WithName, WithFieldDefinitions<T>, WithDirectives, GraphQLOutputType<T> {
    /**
     * The interfaces applied to this.
     *
     * @since 2.0.0
     */
    val interfaces: List<GraphQLInterfaceType<in T>>

    /**
     * The type getter of this interface.
     *
     * @since 2.0.0
     */
    val typeGetter: GraphQLTypeGetter<T>
}

/**
 * The type getter scope.
 *
 * @param T the runtime instance type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLTypeGetterScope<T : Any> {
    /**
     * The instance.
     */
    val instance: T

    /**
     * The context variables that have been
     * provided before the execution of the query.
     */
    val context: Map<Any?, Any?>
}

/**
 * A type getter function type.
 *
 * @param T the runtime instance type.
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLTypeGetter<T> =
        GraphQLTypeGetterScope<T>.() -> GraphQLObjectType<out T>

/**
 * A block of code invoked to fill in options in
 * [GraphQLInterfaceTypeBuilder].
 */
typealias GraphQLInterfaceTypeBlock<T> =
        GraphQLInterfaceTypeBuilder<T>.() -> Unit

/**
 * A block of code invoked to fill in options in
 * [GraphQLInterfaceTypeBuilder].
 */
@Deprecated("Use shorter name instead", ReplaceWith(
    "GraphQLInterfaceTypeBlock<T>",
    "org.cufy.graphkt.schema.GraphQLInterfaceTypeBlock"
))
typealias GraphQLInterfaceTypeBuilderBlock<T> =
        GraphQLInterfaceTypeBuilder<T>.() -> Unit

/**
 * A builder for creating a [GraphQLInterfaceType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInterfaceTypeBuilder<T : Any> :
    WithNameBuilder,
    WithTypeGetterBuilder<T>,
    WithDirectivesBuilder,
    WithInterfacesBuilder<T>,
    WithFieldsBuilder<T>,
    WithDeferredBuilder {

    /**
     * Build the type.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLInterfaceType<T>
}

/* ============= ------------------ ============= */

/**
 * Construct a new [GraphQLTypeGetterScope] with
 * the given arguments.
 */
@OptIn(InternalGraphktApi::class)
fun <T : Any> GraphQLTypeGetterScope(
    instance: T,
    context: Map<*, *>,
): GraphQLTypeGetterScope<T> {
    return GraphQLTypeGetterScopeImpl(
        instance,
        context
    )
}

/**
 * Obtain a new [GraphQLInterfaceTypeBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T : Any> GraphQLInterfaceTypeBuilder(): GraphQLInterfaceTypeBuilder<T> {
    return GraphQLInterfaceTypeBuilderImpl()
}

/**
 * Construct a new [GraphQLInterfaceType] with the
 * given [block].
 *
 * When self referencing, you might use `by`:
 *
 * ```
 * val MyObjectType: GraphQLInterfaceType<T> by GraphQLInterfaceType {
 *      // ...
 * }
 * ```
 *
 * @param name the initial name.
 * @param block the builder block.
 * @return a new interface type.
 * @since 2.0.0
 */
fun <T : Any> GraphQLInterfaceType(
    name: String? = null,
    block: GraphQLInterfaceTypeBlock<T>
): GraphQLInterfaceType<T> {
    val builder = GraphQLInterfaceTypeBuilder<T>()
    name?.let { builder.name(it) }
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */
