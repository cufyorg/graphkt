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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.cufy.graphkt.*
import org.cufy.graphkt.internal.GraphQLFieldDefinitionBuilderImpl
import org.cufy.graphkt.internal.GraphQLGetterScopeImpl
import kotlin.reflect.KProperty1

/* ============= ------------------ ============= */

/**
 * A definition of a field in an object type.
 *
 * @param T the type of the containing object.
 * @param M the type of the field's value.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLFieldDefinition<T : Any, M> : WithName, WithArgumentDefinitions, WithDirectives {
    /**
     * The type of the value of the field.
     *
     * @since 2.0.0
     */
    val type: GraphQLOutputType<M>

    /**
     * The field resolver.
     *
     * @since 2.0.0
     */
    val getter: GraphQLFlowGetter<T, M>
}

/**
 * The value getter scope.
 *
 * @param T the instance type.
 * @param M the value type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLGetterScope<T : Any, M> {
    /**
     * The scope's instance.
     */
    val instance: T

    /**
     * The arguments passed to the field.
     */
    val arguments: List<GraphQLArgument<*>>

    /**
     * The directives applied to the field.
     */
    val directives: List<GraphQLDirective>

    /**
     * The context variables that have been
     * provided before the execution of the query.
     */
    val context: Map<Any?, Any?>

    /**
     * The context variables that have been passed
     * by the containing field.
     */
    val local: Map<Any?, Any?>

    /**
     * The context variables to be passed to the
     * contained fields.
     */
    val subLocal: MutableMap<Any?, Any?>

    // TODO change to extensions when multiple contexts are supported

    /**
     * Get the directive with this definition.
     */
    operator fun GraphQLDirectiveDefinition.invoke(): GraphQLDirective {
        return directives.first { it.definition.name == this.name }
    }

    /**
     * Get the value of this argument.
     */
    operator fun <T> GraphQLArgumentDefinition<T>.invoke(): T {
        @Suppress("UNCHECKED_CAST")
        return arguments.first { it.definition.name == this.name }.value as T
    }

    /**
     * Get the value of this argument mapped with the given [mapper].
     */
    fun <T, U> GraphQLArgumentDefinition<T>.map(mapper: (T) -> U): U {
        return this().let(mapper)
    }

    /**
     * Get the value of this argument mapped with the given [mapper].
     *
     * If the value is null, the mapping is skipped and `null` is returned instead.
     */
    fun <T, U> GraphQLArgumentDefinition<T>.mapNotNull(mapper: (T & Any) -> U): U? {
        return map { it?.let(mapper) }
    }
}

/**
 * A value getter function type. (single)
 *
 * @param T the instance type.
 * @param M the value type.
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLGetter<T, M> =
        suspend GraphQLGetterScope<T, M>.() -> M

/**
 * A value getter function type.
 *
 * @param T the instance type.
 * @param M the value type.
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLFlowGetter<T, M> =
        suspend GraphQLGetterScope<T, M>.() -> Flow<M>

/**
 * A value getter block function type.
 *
 * @param T the instance type.
 * @param M the value type.
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLGetterBlock<T, M> =
        suspend GraphQLGetterScope<T, M>.() -> Unit

/**
 * A block of code invoked to fill in options in
 * [GraphQLFieldDefinitionBuilder].
 */
typealias GraphQLFieldDefinitionBuilderBlock<T, M> =
        GraphQLFieldDefinitionBuilder<T, M>.() -> Unit

/**
 * A builder for creating a [GraphQLFieldDefinition].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLFieldDefinitionBuilder<T : Any, M> :
    WithNameBuilder,
    WithOutputTypeBuilder<M>,
    WithDirectivesBuilder,
    WithArgumentDefinitionsBuilder,
    WithDeferredBuilder {
    /**
     * Blocks of code to be invoked before
     * invoking the getter.
     */
    @AdvancedGraphktApi("Use `onGet()` instead")
    val getterBlocks: MutableList<GraphQLGetterBlock<T, M>>

    /**
     * The value getter.
     */
    @AdvancedGraphktApi("Use `get()` instead")
    var getter: GraphQLFlowGetter<T, M>? // REQUIRED

    /**
     * Build the definition.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLFieldDefinition<T, M>
}

/* ============= ------------------ ============= */

/**
 * Construct a new [GraphQLGetterScope] with the
 * given arguments.
 */
@OptIn(InternalGraphktApi::class)
fun <T : Any, M> GraphQLGetterScope(
    instance: T,
    arguments: List<GraphQLArgument<*>>,
    directives: List<GraphQLDirective>,
    context: Map<Any?, Any?>,
    local: Map<Any?, Any?>,
    subLocal: MutableMap<Any?, Any?>
): GraphQLGetterScope<T, M> {
    return GraphQLGetterScopeImpl(
        instance,
        arguments,
        directives,
        context,
        local,
        subLocal
    )
}

/**
 * Obtain a new [GraphQLFieldDefinitionBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T : Any, M> GraphQLFieldDefinitionBuilder(): GraphQLFieldDefinitionBuilder<T, M> {
    return GraphQLFieldDefinitionBuilderImpl()
}

/**
 * Construct a new [GraphQLFieldDefinition] with the
 * given [block].
 *
 * @param name the initial name.
 * @param type the initial type.
 * @param block the builder block.
 * @return a new field definition.
 * @since 2.0.0
 */
fun <T : Any, M> GraphQLFieldDefinition(
    name: String? = null,
    type: GraphQLOutputType<M>? = null,
    block: GraphQLFieldDefinitionBuilderBlock<T, M> = {}
): GraphQLFieldDefinition<T, M> {
    val builder = GraphQLFieldDefinitionBuilder<T, M>()
    name?.let { builder.name(it) }
    type?.let { builder.type { it } }
    builder.apply(block)
    return builder.build()
}

/**
 * Construct a new [GraphQLFieldDefinition] with
 * the [property] and [block].
 *
 * @param property the property.
 * @param type the initial type.
 * @param block the builder block.
 * @return a new field definition.
 * @since 2.0.0
 */
@Suppress("FunctionName")
fun <T : Any, M> GraphQLPropertyFieldDefinition(
    property: KProperty1<T, M>,
    type: GraphQLOutputType<M>? = null,
    block: GraphQLFieldDefinitionBuilderBlock<T, M> = {}
): GraphQLFieldDefinition<T, M> {
    val builder = GraphQLFieldDefinitionBuilder<T, M>()
    type?.let { builder.type { it } }
    builder.property(property)
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */

//

/**
 * Set the name and getter of the definition from
 * the given [property].
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any, M> GraphQLFieldDefinitionBuilder<T, M>.property(
    property: KProperty1<T, M>
) {
    this.name = property.name
    this.getter = {
        flowOf(property.getter(instance))
    }
}

// getterBlock

/**
 * Add the given [block] to be invoked before
 * invoking the getter.
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any, M> GraphQLFieldDefinitionBuilder<T, M>.onGet(
    block: GraphQLGetterBlock<T, M>
) {
    this.getterBlocks += block
}

// getter

/**
 * Set the getter of this field to the given [getter].
 *
 * ---
 *
 * Note: If this field is not a root subscription
 * field, the given [getter] must return a flow
 * with a single item.
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any, M> GraphQLFieldDefinitionBuilder<T, M>.getFlow(
    getter: GraphQLFlowGetter<T, M>
) {
    this.getter = getter
}

/**
 * Set the getter of this field to the given [getter].
 *
 * For root subscription fields, it is best to use [getFlow].
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any, M> GraphQLFieldDefinitionBuilder<T, M>.get(
    getter: GraphQLGetter<T, M>
) {
    this.getter = { flowOf(getter()) }
}

/* ============= ------------------ ============= */
