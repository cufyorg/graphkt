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

import kotlinx.coroutines.flow.flowOf
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

/* ============================================== */
/* ========|                            |======== */
/* ========| Argument                   |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance of some argument.
 *
 * @param T the argument's value type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLArgument<T> {
    /**
     * The argument's definition.
     */
    val definition: GraphQLArgumentDefinition<T>

    /**
     * The argument's value.
     */
    val value: T
}

/**
 * Construct a new [GraphQLArgument].
 *
 * @param definition the argument definition.
 * @param value the value.
 * @since 2.0.0
 */
fun <T> GraphQLArgument(
    definition: GraphQLArgumentDefinition<T>,
    value: T
): GraphQLArgument<T> {
    return object : GraphQLArgument<T> {
        override val definition = definition
        override val value = value

        override fun toString() = "GraphQLArgument($value, $definition)"
    }
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Argument Definition        |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A definition of an argument.
 *
 * @param T the type of the argument's value.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLArgumentDefinition<T> :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives,
    GraphQLElementWithInputType<T>

/**
 * A mutable variant of [GraphQLArgumentDefinition].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableArgumentDefinition<T> :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLMutableElementWithInputType<T>,
    GraphQLArgumentDefinition<T>

/**
 * Construct a new [GraphQLArgumentDefinition] with the given arguments.
 */
fun <T> GraphQLArgumentDefinition(
    name: String,
    description: String,
    directives: List<GraphQLDirective>,
    type: Lazy<GraphQLInputType<T>>
): GraphQLArgumentDefinition<T> {
    return object : GraphQLArgumentDefinition<T> {
        override val name = name
        override val description = description
        override val directives = directives
        override val type = type

        override fun toString() = "GraphQLArgumentDefinition($name, $type)"
    }
}

/**
 * Obtain a new instance of [GraphQLMutableArgumentDefinition].
 */
fun <T> GraphQLMutableArgumentDefinition(): GraphQLMutableArgumentDefinition<T> {
    return object : GraphQLMutableArgumentDefinition<T> {
        override lateinit var name: String
        override var description: String = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override lateinit var type: Lazy<GraphQLInputType<T>>

        override fun toString() = "GraphQLMutableArgumentDefinition($name, $type)"
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun <T> GraphQLArgumentDefinition<T>.copy(
    name: String = this.name,
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    type: Lazy<GraphQLInputType<T>> = this.type
): GraphQLArgumentDefinition<T> {
    return GraphQLArgumentDefinition(
        name = name,
        description = description,
        directives = directives.toList(),
        type = type
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableArgumentDefinition].
 */
typealias GraphQLArgumentDefinitionBlock<T> =
        GraphQLMutableArgumentDefinition<T>.() -> Unit

/**
 * Construct a new [GraphQLArgumentDefinition] with the
 * given [block].
 *
 * @param name the initial name.
 * @param type the initial type.
 * @param block the builder block.
 * @return a new argument definition.
 * @since 2.0.0
 */
fun <T> GraphQLArgumentDefinition(
    name: String? = null,
    type: GraphQLInputType<T>? = null,
    block: GraphQLArgumentDefinitionBlock<T> = {}
): GraphQLArgumentDefinition<T> {
    val element = GraphQLMutableArgumentDefinition<T>()
    name?.let { element.name = it }
    type?.let { element.type = lazy { it } }
    element.apply(block)
    return element.copy()
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Directive                  |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance of some directive.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLDirective :
    GraphQLElementWithArguments {

    /**
     * The definition of the directive.
     *
     * @since 2.0.0
     */
    val definition: GraphQLDirectiveDefinition
}

/**
 * A mutable variant of [GraphQLDirective].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableDirective :
    GraphQLMutableElementWithArguments,
    GraphQLDirective {

    override /* lateinit */ var definition: GraphQLDirectiveDefinition
}

/**
 * Construct a new [GraphQLDirective].
 *
 * @param definition the directive definition.
 * @param arguments the arguments.
 * @since 2.0.0
 */
fun GraphQLDirective(
    definition: GraphQLDirectiveDefinition,
    arguments: List<GraphQLArgument<*>>
): GraphQLDirective {
    return object : GraphQLDirective {
        override val definition = definition
        override val arguments = arguments
    }
}

/**
 * Obtain a new [GraphQLMutableDirective].
 *
 * @since 2.0.0
 */
fun GraphQLMutableDirective(): GraphQLMutableDirective {
    return object : GraphQLMutableDirective {
        override lateinit var definition: GraphQLDirectiveDefinition
        override val arguments = mutableListOf<GraphQLArgument<*>>()

        override fun toString() = "GraphQLMutableDirective($definition)"
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun GraphQLDirective.copy(
    definition: GraphQLDirectiveDefinition = this.definition,
    arguments: List<GraphQLArgument<*>> = this.arguments
): GraphQLDirective {
    return GraphQLDirective(
        definition = definition,
        arguments = arguments.toList()
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableDirective].
 */
typealias GraphQLDirectiveBlock =
        GraphQLMutableDirective.() -> Unit

/**
 * Construct a new [GraphQLDirective] with the
 * given [block].
 *
 * @param definition the initial definition.
 * @param arguments the initial arguments.
 * @param block the builder block.
 * @return a new directive.
 * @since 2.0.0
 */
fun GraphQLDirective(
    definition: GraphQLDirectiveDefinition? = null,
    vararg arguments: GraphQLArgument<*>,
    block: GraphQLDirectiveBlock
): GraphQLDirective {
    val element = GraphQLMutableDirective()
    definition?.let { element.definition = it }
    element.arguments += arguments
    element.apply(block)
    return element.copy()
}

/* ---------------------------------------------- */

/**
 * Set the definition to the given [definition].
 *
 * This will replace the current definition.
 */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("replace with `this.definition = definition`")
fun GraphQLMutableDirective.definition(
    definition: GraphQLDirectiveDefinition
) {
    this.definition = definition
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Directive Definition       |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A definition of a graphql directive.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLDirectiveDefinition :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithArgumentDefinitions {
    /**
     * True, if this directive is repeatable.
     */
    val repeatable: Boolean

    /**
     * The locations this directive can be placed at.
     *
     * @since 2.0.0
     */
    val locations: Set<GraphQLDirectiveLocation>
}

/**
 * A builder for creating a [GraphQLDirectiveDefinition].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableDirectiveDefinition :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithArgumentDefinitions,
    GraphQLDirectiveDefinition {

    override var repeatable: Boolean /* = false */
    override val locations: MutableSet<GraphQLDirectiveLocation> /* = mutableSetOf() */
}

/**
 * Construct a new [GraphQLDirectiveDefinition] with the given arguments.
 */
fun GraphQLDirectiveDefinition(
    name: String,
    description: String,
    arguments: List<GraphQLArgumentDefinition<*>>,
    repeatable: Boolean,
    locations: Set<GraphQLDirectiveLocation>
): GraphQLDirectiveDefinition {
    return object : GraphQLDirectiveDefinition {
        override val name = name
        override val description = description
        override val repeatable = repeatable
        override val locations = locations
        override val arguments = arguments

        override fun toString() = "GraphQLDirectiveDefinition($name)"
    }
}

/**
 * Obtain a new [GraphQLMutableDirectiveDefinition].
 *
 * @since 2.0.0
 */
fun GraphQLMutableDirectiveDefinition(): GraphQLMutableDirectiveDefinition {
    return object : GraphQLMutableDirectiveDefinition {
        override lateinit var name: String
        override var description: String = ""
        override val arguments = mutableListOf<GraphQLArgumentDefinition<*>>()
        override var repeatable = false
        override val locations = mutableSetOf<GraphQLDirectiveLocation>()

        override fun toString() = "GraphQLMutableDirectiveDefinition($name)"
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun GraphQLDirectiveDefinition.copy(
    name: String = this.name,
    description: String = this.description,
    arguments: List<GraphQLArgumentDefinition<*>> = this.arguments,
    repeatable: Boolean = this.repeatable,
    locations: Set<GraphQLDirectiveLocation> = this.locations
): GraphQLDirectiveDefinition {
    return GraphQLDirectiveDefinition(
        name = name,
        description = description,
        arguments = arguments.toList(),
        repeatable = repeatable,
        locations = locations.toSet()
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableDirectiveDefinition].
 */
typealias GraphQLDirectiveDefinitionBlock =
        GraphQLMutableDirectiveDefinition.() -> Unit

/**
 * Construct a new [GraphQLDirectiveDefinition] with the
 * given [block].
 *
 * @param name the initial name.
 * @param block the builder block.
 * @return a new directive definition.
 * @since 2.0.0
 */
fun GraphQLDirectiveDefinition(
    name: String? = null,
    block: GraphQLDirectiveDefinitionBlock = {}
): GraphQLDirectiveDefinition {
    val element = GraphQLMutableDirectiveDefinition()
    name?.let { element.name = it }
    element.apply(block)
    return element.copy()
}

/* ---------------------------------------------- */

/**
 * Add the given [location].
 */
fun GraphQLMutableDirectiveDefinition.location(
    location: GraphQLDirectiveLocation
) {
    locations += location
}

/**
 * Set the repeatable property to the given [value].
 */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("replace with `this.repeatable = value`")
fun GraphQLMutableDirectiveDefinition.repeatable(
    value: Boolean = true
) {
    repeatable = value
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Enum Type                  |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * The type of a graphql enum.
 *
 * @param T the enum's runtime value.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLEnumType<T> :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives,
    GraphQLInputOutputType<T> {

    /**
     * The enum's values.
     */
    val values: List<GraphQLEnumValueDefinition<T>>
}

/**
 * A mutable variant of [GraphQLEnumType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableEnumType<T> :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLEnumType<T> {

    override val values: MutableList<GraphQLEnumValueDefinition<T>>
}

/**
 * Construct a new [GraphQLEnumType] with the given arguments.
 */
fun <T> GraphQLEnumType(
    name: String,
    description: String,
    directives: List<GraphQLDirective>,
    values: List<GraphQLEnumValueDefinition<T>>
): GraphQLEnumType<T> {
    return object : GraphQLEnumType<T> {
        override val name = name
        override val description = description
        override val directives = directives
        override val values = values
    }
}

/**
 * Obtain a new [GraphQLMutableEnumType].
 *
 * @since 2.0.0
 */
fun <T> GraphQLMutableEnumType(): GraphQLMutableEnumType<T> {
    return object : GraphQLMutableEnumType<T> {
        override lateinit var name: String
        override var description: String = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override val values = mutableListOf<GraphQLEnumValueDefinition<T>>()
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun <T> GraphQLEnumType<T>.copy(
    name: String = this.name,
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    values: List<GraphQLEnumValueDefinition<T>> = this.values
): GraphQLEnumType<T> {
    return GraphQLEnumType(
        name = name,
        description = description,
        directives = directives.toList(),
        values = values.toList()
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableEnumType].
 */
typealias GraphQLEnumTypeBlock<T> =
        GraphQLMutableEnumType<T>.() -> Unit

/**
 * Construct a new [GraphQLEnumType] with the
 * given [block].
 *
 * @param name the initial name.
 * @param block the builder block.
 * @return a new enum type.
 * @since 2.0.0
 */
fun <T> GraphQLEnumType(
    name: String? = null,
    block: GraphQLEnumTypeBlock<T> = {}
): GraphQLEnumType<T> {
    val element = GraphQLMutableEnumType<T>()
    name?.let { element.name = it }
    element.apply(block)
    return element.copy()
}

/* ---------------------------------------------- */

/**
 * Add the given value [definition].
 */
fun <T> GraphQLMutableEnumType<T>.value(
    definition: GraphQLEnumValueDefinition<T>
) {
    values += definition
}

/**
 * Add a value definition with the given arguments.
 *
 * @param name the initial name.
 * @param value the initial value. (null if unset)
 * @param block the builder block.
 * @since 2.0.0
 */
fun <T> GraphQLMutableEnumType<T>.value(
    name: String? = null,
    value: T? = null,
    block: GraphQLEnumValueDefinitionBlock<T> = {}
) {
    value(GraphQLEnumValueDefinition(name, value, block))
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Enum Value Definition      |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A definition of an enum value.
 *
 * @param T the runtime value type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLEnumValueDefinition<T> :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives {

    /**
     * The enum's runtime value.
     */
    val value: Lazy<T>
}

/**
 * A mutable variant of [GraphQLEnumValueDefinition].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableEnumValueDefinition<T> :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLEnumValueDefinition<T> {

    override /* lateinit */ var value: Lazy<T>
}

/**
 * Construct a new [GraphQLEnumValueDefinition] with the given arguments.
 */
fun <T> GraphQLEnumValueDefinition(
    name: String,
    description: String,
    directives: List<GraphQLDirective>,
    value: Lazy<T>
): GraphQLEnumValueDefinition<T> {
    return object : GraphQLEnumValueDefinition<T> {
        override val name = name
        override val description = description
        override val directives = directives
        override val value = value
    }
}

/**
 * Obtain a new [GraphQLMutableEnumValueDefinition].
 *
 * @since 2.0.0
 */
fun <T> GraphQLMutableEnumValueDefinition(): GraphQLMutableEnumValueDefinition<T> {
    return object : GraphQLMutableEnumValueDefinition<T> {
        override lateinit var name: String
        override var description = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override lateinit var value: Lazy<T>

        override fun toString() = "GraphQLMutableEnumValueDefinition($name, $value)"
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun <T> GraphQLEnumValueDefinition<T>.copy(
    name: String = this.name,
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    value: Lazy<T> = this.value
): GraphQLEnumValueDefinition<T> {
    return GraphQLEnumValueDefinition(
        name = name,
        description = description,
        directives = directives,
        value = value
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableEnumValueDefinition].
 */
typealias GraphQLEnumValueDefinitionBlock<T> =
        GraphQLMutableEnumValueDefinition<T>.() -> Unit

/**
 * Construct a new [GraphQLEnumValueDefinition] with the
 * given [block].
 *
 * @param name the initial name.
 * @param value the initial value. (null if unset)
 * @param block the builder block.
 * @return a new field definition.
 * @since 2.0.0
 */
fun <T> GraphQLEnumValueDefinition(
    name: String? = null,
    value: T? = null,
    block: GraphQLEnumValueDefinitionBlock<T> = {}
): GraphQLEnumValueDefinition<T> {
    val element = GraphQLMutableEnumValueDefinition<T>()
    name?.let { element.name = it }
    value?.let { element.value = lazy { it } }
    element.apply(block)
    return element.copy()
}

/* ---------------------------------------------- */

/**
 * Set the enum's value to be the result of
 * invoking the given [block].
 */
fun <T> GraphQLMutableEnumValueDefinition<T>.value(
    block: () -> T
) {
    value = lazy(block)
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Field Definition           |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A definition of a field in an object type.
 *
 * @param T the type of the containing object.
 * @param M the type of the field's value.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLFieldDefinition<T : Any, M> :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives,
    GraphQLElementWithOutputType<M>,
    GraphQLElementWithArgumentDefinitions {

    /**
     * A block of code to be invoked before the
     * getter.
     *
     * @since 2.0.0
     */
    val onGetBlocks: List<GraphQLGetterBlock<T, M>>

    /**
     * A blocking block of code to be invoked
     * before the getter.
     *
     * @since 2.0.0
     */
    val onGetBlockingBlocks: List<GraphQLGetterBlockingBlock<T, M>>

    /**
     * The field resolver (getter).
     *
     * @since 2.0.0
     */
    val getter: GraphQLFlowGetter<T, M>
}

/**
 * A mutable variant of [GraphQLFieldDefinition].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableFieldDefinition<T : Any, M> :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLMutableElementWithOutputType<M>,
    GraphQLMutableElementWithArgumentDefinitions,
    GraphQLFieldDefinition<T, M> {

    override val onGetBlocks: MutableList<GraphQLGetterBlock<T, M>> /* = mutableListOf() */
    override val onGetBlockingBlocks: MutableList<GraphQLGetterBlockingBlock<T, M>> /* = mutableListOf() */
    override var getter: GraphQLFlowGetter<T, M> /* = { throw NotImplementedError() } */
}

/**
 * Construct a new [GraphQLFieldDefinition] with the given arguments.
 */
fun <T : Any, M> GraphQLFieldDefinition(
    name: String,
    description: String,
    directives: List<GraphQLDirective>,
    type: Lazy<GraphQLOutputType<M>>,
    arguments: List<GraphQLArgumentDefinition<*>>,
    onGetBlocks: List<GraphQLGetterBlock<T, M>>,
    onGetBlockingBlocks: List<GraphQLGetterBlockingBlock<T, M>>,
    getter: GraphQLFlowGetter<T, M>
): GraphQLFieldDefinition<T, M> {
    return object : GraphQLFieldDefinition<T, M> {
        override val name = name
        override val description = description
        override val directives = directives
        override val type = type
        override val arguments = arguments
        override val onGetBlocks = onGetBlocks
        override val onGetBlockingBlocks = onGetBlockingBlocks
        override val getter = getter
    }
}

/**
 * Obtain a new [GraphQLMutableFieldDefinition].
 *
 * @since 2.0.0
 */
fun <T : Any, M> GraphQLMutableFieldDefinition(): GraphQLMutableFieldDefinition<T, M> {
    return object : GraphQLMutableFieldDefinition<T, M> {
        override lateinit var name: String
        override var description: String = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override lateinit var type: Lazy<GraphQLOutputType<M>>
        override val arguments = mutableListOf<GraphQLArgumentDefinition<*>>()
        override val onGetBlocks = mutableListOf<GraphQLGetterBlock<T, M>>()
        override val onGetBlockingBlocks = mutableListOf<GraphQLGetterBlockingBlock<T, M>>()
        override var getter: GraphQLFlowGetter<T, M> = {
            throw NotImplementedError("Getter of field $name was not implemented.")
        }
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun <T : Any, M> GraphQLFieldDefinition<T, M>.copy(
    name: String = this.name,
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    type: Lazy<GraphQLOutputType<M>> = this.type,
    arguments: List<GraphQLArgumentDefinition<*>> = this.arguments,
    onGetBlocks: List<GraphQLGetterBlock<T, M>> = this.onGetBlocks,
    onGetBlockingBlocks: List<GraphQLGetterBlockingBlock<T, M>> = this.onGetBlockingBlocks,
    getter: GraphQLFlowGetter<T, M> = this.getter
): GraphQLFieldDefinition<T, M> {
    return GraphQLFieldDefinition(
        name = name,
        description = description,
        directives = directives,
        type = type,
        arguments = arguments,
        onGetBlocks = onGetBlocks,
        onGetBlockingBlocks = onGetBlockingBlocks,
        getter = getter
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableFieldDefinition].
 */
typealias GraphQLFieldDefinitionBlock<T, M> =
        GraphQLMutableFieldDefinition<T, M>.() -> Unit

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
    block: GraphQLFieldDefinitionBlock<T, M> = {}
): GraphQLFieldDefinition<T, M> {
    val element = GraphQLMutableFieldDefinition<T, M>()
    name?.let { element.name = it }
    type?.let { element.type = lazy { it } }
    element.apply(block)
    return element.copy()
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
fun <T : Any, M> GraphQLFieldDefinition(
    property: KProperty1<in T, M>,
    type: GraphQLOutputType<M>? = null,
    block: GraphQLFieldDefinitionBlock<T, M> = {}
): GraphQLFieldDefinition<T, M> {
    val element = GraphQLMutableFieldDefinition<T, M>()
    type?.let { element.type = lazy { it } }
    element.property(property)
    element.apply(block)
    return element.copy()
}

/* ---------------------------------------------- */

/**
 * Set the name and getter of the definition from
 * the given [property].
 */
fun <T : Any, M> GraphQLMutableFieldDefinition<T, M>.property(
    property: KProperty1<in T, M>
) {
    this.name = property.name
    this.getter = {
        flowOf(property.getter(instance))
    }
}

/**
 * Add the given [block] to be invoked before
 * invoking the getter.
 */
fun <T : Any, M> GraphQLMutableFieldDefinition<T, M>.onGet(
    block: GraphQLGetterBlock<T, M>
) {
    this.onGetBlocks += block
}

/**
 * Add the given [block] to be invoked before
 * invoking the getter.
 *
 * Blocking onGet blocks are invoked before
 * non-blocking onGet blocks.
 */
fun <T : Any, M> GraphQLMutableFieldDefinition<T, M>.onGetBlocking(
    block: GraphQLGetterBlockingBlock<T, M>
) {
    this.onGetBlockingBlocks += block
}

/**
 * Set the getter of this field to the given [getter].
 *
 * ---
 *
 * Note: If this field is not a root subscription
 * field, the given [getter] must return a flow
 * with a single item.
 *
 * Note: the errors from the [getter] will be
 * caught and sent to the client. But, the errors
 * from the flow returned from the [getter] will
 * not.
 *
 * **The getter of interface fields will be ignored**
 */
fun <T : Any, M> GraphQLMutableFieldDefinition<T, M>.getFlow(
    getter: GraphQLFlowGetter<T, M>
) {
    this.getter = getter
}

/**
 * Set the getter of this field to the given [getter].
 *
 * For root subscription fields, it is best to use [getFlow].
 *
 * **The getter of interface fields will be ignored**
 */
fun <T : Any, M> GraphQLMutableFieldDefinition<T, M>.get(
    getter: GraphQLGetter<T, M>
) {
    this.getter = { flowOf(getter()) }
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Input Field Definition     |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A definition of a field in an input object type.
 *
 * @param T the type of the containing object.
 * @param M the type of the field's value.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputFieldDefinition<T : Any, M> :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives,
    GraphQLElementWithInputType<M> {

    /**
     * The field's getter.
     *
     * @since 2.0.0
     */
    val getter: GraphQLInputGetter<T, M>

    /**
     * The field's setter.
     *
     * @since 2.0.0
     */
    val setter: GraphQLInputSetter<T, M>
}

/**
 * A mutable variant of [GraphQLInputFieldDefinition].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableInputFieldDefinition<T : Any, M> :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLMutableElementWithInputType<M>,
    GraphQLInputFieldDefinition<T, M> {

    override /* lateinit */ var getter: GraphQLInputGetter<T, M>
    override /* lateinit */ var setter: GraphQLInputSetter<T, M>
}

/**
 * Construct a new [GraphQLInputFieldDefinition] with the given arguments.
 */
fun <T : Any, M> GraphQLInputFieldDefinition(
    name: String,
    description: String,
    directives: List<GraphQLDirective>,
    type: Lazy<GraphQLInputType<M>>,
    getter: GraphQLInputGetter<T, M>,
    setter: GraphQLInputSetter<T, M>
): GraphQLInputFieldDefinition<T, M> {
    return object : GraphQLInputFieldDefinition<T, M> {
        override val name = name
        override val description = description
        override val directives = directives
        override val type = type
        override val getter = getter
        override val setter = setter
    }
}

/**
 * Obtain a new [GraphQLMutableInputFieldDefinition].
 *
 * @since 2.0.0
 */
fun <T : Any, M> GraphQLMutableInputFieldDefinition(): GraphQLMutableInputFieldDefinition<T, M> {
    return object : GraphQLMutableInputFieldDefinition<T, M> {
        override lateinit var name: String
        override var description: String = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override lateinit var type: Lazy<GraphQLInputType<M>>
        override lateinit var getter: GraphQLInputGetter<T, M>
        override lateinit var setter: GraphQLInputSetter<T, M>
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun <T : Any, M> GraphQLInputFieldDefinition<T, M>.copy(
    name: String = this.name,
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    type: Lazy<GraphQLInputType<M>> = this.type,
    getter: GraphQLInputGetter<T, M> = this.getter,
    setter: GraphQLInputSetter<T, M> = this.setter
): GraphQLInputFieldDefinition<T, M> {
    return GraphQLInputFieldDefinition(
        name = name,
        description = description,
        directives = directives,
        type = type,
        getter = getter,
        setter = setter
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableInputFieldDefinition].
 */
typealias GraphQLInputFieldDefinitionBlock<T, M> =
        GraphQLMutableInputFieldDefinition<T, M>.() -> Unit

/**
 * Construct a new [GraphQLInputFieldDefinition] with the
 * given [block].
 *
 * @param name the initial name.
 * @param type the initial type.
 * @param block the builder block.
 * @return a new input field definition.
 * @since 2.0.0
 */
fun <T : Any, M> GraphQLInputFieldDefinition(
    name: String? = null,
    type: GraphQLInputType<M>? = null,
    block: GraphQLInputFieldDefinitionBlock<T, M> = {}
): GraphQLInputFieldDefinition<T, M> {
    val element = GraphQLMutableInputFieldDefinition<T, M>()
    name?.let { element.name = (it) }
    type?.let { element.type = lazy { it } }
    element.apply(block)
    return element.copy()
}

/**
 * Construct a new [GraphQLInputFieldDefinition] with
 * the [property] and [block].
 *
 * @param property the property.
 * @param type the initial type.
 * @param block the builder block.
 * @return a new field definition.
 * @since 2.0.0
 */
fun <T : Any, M> GraphQLInputFieldDefinition(
    property: KMutableProperty1<in T, M>,
    type: GraphQLInputType<M>? = null,
    block: GraphQLInputFieldDefinitionBlock<T, M> = {}
): GraphQLInputFieldDefinition<T, M> {
    val element = GraphQLMutableInputFieldDefinition<T, M>()
    type?.let { element.type = lazy { it } }
    element.property(property)
    element.apply(block)
    return element.copy()
}

/* ---------------------------------------------- */

/**
 * Set the name and setter of the definition from
 * the given [property].
 */
fun <T : Any, M> GraphQLMutableInputFieldDefinition<T, M>.property(
    property: KMutableProperty1<in T, M>
) {
    this.name = property.name
    this.getter = property.getter
    this.setter = property.setter
}

/**
 * Set the getter of this field to the given [getter].
 *
 * This will replace the current getter.
 */
fun <T : Any, M> GraphQLMutableInputFieldDefinition<T, M>.get(
    getter: GraphQLInputGetter<T, M>
) {
    this.getter = getter
}

/**
 * Set the setter of this field to the given [setter].
 *
 * This will replace the current setter.
 */
fun <T : Any, M> GraphQLMutableInputFieldDefinition<T, M>.set(
    setter: GraphQLInputSetter<T, M>
) {
    this.setter = setter
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Input Object Type          |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A type for GraphQL input objects.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputObjectType<T : Any> :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives,
    GraphQLElementWithInputFieldDefinitions<T>,
    GraphQLInputType<T> {

    /**
     * The constructor of the runtime value [T].
     */
    val constructor: GraphQLInputConstructor<T>
}

/**
 * A mutable variant of [GraphQLInputObjectType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableInputObjectType<T : Any> :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLMutableElementWithInputFieldDefinitions<T>,
    GraphQLInputObjectType<T> {

    override /* lateinit */ var constructor: GraphQLInputConstructor<T>
}

/**
 * Construct a new [GraphQLInputObjectType] with the given arguments.
 */
fun <T : Any> GraphQLInputObjectType(
    name: String,
    description: String,
    directives: List<GraphQLDirective>,
    fields: List<GraphQLInputFieldDefinition<T, *>>,
    constructor: GraphQLInputConstructor<T>
): GraphQLInputObjectType<T> {
    return object : GraphQLInputObjectType<T> {
        override val name = name
        override val description = description
        override val directives = directives
        override val fields = fields
        override val constructor = constructor
    }
}

/**
 * Obtain a new [GraphQLMutableInputObjectType].
 *
 * @since 2.0.0
 */
fun <T : Any> GraphQLMutableInputObjectType(): GraphQLMutableInputObjectType<T> {
    return object : GraphQLMutableInputObjectType<T> {
        override lateinit var name: String
        override var description: String = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override val fields = mutableListOf<GraphQLInputFieldDefinition<T, *>>()
        override lateinit var constructor: GraphQLInputConstructor<T>
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun <T : Any> GraphQLInputObjectType<T>.copy(
    name: String = this.name,
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    fields: List<GraphQLInputFieldDefinition<T, *>> = this.fields,
    constructor: GraphQLInputConstructor<T> = this.constructor
): GraphQLInputObjectType<T> {
    return GraphQLInputObjectType(
        name = name,
        description = description,
        directives = directives,
        fields = fields,
        constructor = constructor
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableInputObjectType].
 */
typealias GraphQLInputObjectTypeBlock<T> =
        GraphQLMutableInputObjectType<T>.() -> Unit

/**
 * Construct a new [GraphQLInputObjectType] with the
 * given [block].
 *
 * When self referencing, you might use `by`:
 *
 * ```
 * val MyObjectType: GraphQLInputObjectType<T> by GraphQLInputObjectType {
 *      // ...
 * }
 * ```
 *
 * @param name the initial name.
 * @param constructor the initial constructor.
 * @param block the builder block.
 * @return a new object type.
 * @since 2.0.0
 */
fun <T : Any> GraphQLInputObjectType(
    name: String? = null,
    constructor: GraphQLInputConstructor<T>? = null,
    block: GraphQLInputObjectTypeBlock<T>
): GraphQLInputObjectType<T> {
    val element = GraphQLMutableInputObjectType<T>()
    name?.let { element.name = it }
    constructor?.let { element.constructor = it }
    element.apply(block)
    return element.copy()
}

/* ---------------------------------------------- */

/**
 * Set the constructor to the given [constructor].
 *
 * This will replace the current constructor.
 */
fun <T : Any> GraphQLMutableInputObjectType<T>.construct(
    constructor: GraphQLInputConstructor<T>
) {
    this.constructor = constructor
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Interface Type             |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A type for GraphQL interfaces.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInterfaceType<T : Any> :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives,
    GraphQLElementWithInterfaces<T>,
    GraphQLElementWithFieldDefinitions<T>,
    GraphQLElementWithTypeGetter<T>,
    GraphQLOutputType<T>

/**
 * A mutable variant of [GraphQLInterfaceType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableInterfaceType<T : Any> :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLMutableElementWithInterfaces<T>,
    GraphQLMutableElementWithFieldDefinitions<T>,
    GraphQLMutableElementWithTypeGetter<T>,
    GraphQLInterfaceType<T>

/**
 * Construct a new [GraphQLInterfaceType] with the given arguments.
 */
fun <T : Any> GraphQLInterfaceType(
    name: String,
    description: String,
    directives: List<GraphQLDirective>,
    interfaces: List<GraphQLInterfaceType<in T>>,
    onGetBlocks: List<GraphQLGetterBlock<T, *>>,
    onGetBlockingBlocks: List<GraphQLGetterBlockingBlock<T, *>>,
    fields: List<GraphQLFieldDefinition<T, *>>,
    typeGetter: GraphQLTypeGetter<T>
): GraphQLInterfaceType<T> {
    return object : GraphQLInterfaceType<T> {
        override val name = name
        override val description = description
        override val directives = directives
        override val interfaces = interfaces
        override val onGetBlocks = onGetBlocks
        override val onGetBlockingBlocks = onGetBlockingBlocks
        override val fields = fields
        override val typeGetter = typeGetter
    }
}

/**
 * Obtain a new [GraphQLMutableInterfaceType].
 *
 * @since 2.0.0
 */
fun <T : Any> GraphQLMutableInterfaceType(): GraphQLMutableInterfaceType<T> {
    return object : GraphQLMutableInterfaceType<T> {
        override lateinit var name: String
        override var description: String = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override val interfaces = mutableListOf<GraphQLInterfaceType<in T>>()
        override val onGetBlocks = mutableListOf<GraphQLGetterBlock<T, *>>()
        override val onGetBlockingBlocks = mutableListOf<GraphQLGetterBlockingBlock<T, *>>()
        override val fields = mutableListOf<GraphQLFieldDefinition<T, *>>()
        override var typeGetter: GraphQLTypeGetter<T> = {
            throw NotImplementedError("Type getter of interface $name was not implemented.")
        }
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun <T : Any> GraphQLInterfaceType<T>.copy(
    name: String = this.name,
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    interfaces: List<GraphQLInterfaceType<in T>> = this.interfaces,
    onGetBlocks: List<GraphQLGetterBlock<T, *>> = this.onGetBlocks,
    onGetBlockingBlocks: List<GraphQLGetterBlockingBlock<T, *>> = this.onGetBlockingBlocks,
    fields: List<GraphQLFieldDefinition<T, *>> = this.fields,
    typeGetter: GraphQLTypeGetter<T> = this.typeGetter,
): GraphQLInterfaceType<T> {
    return GraphQLInterfaceType(
        name = name,
        description = description,
        directives = directives,
        interfaces = interfaces,
        onGetBlocks = onGetBlocks,
        onGetBlockingBlocks = onGetBlockingBlocks,
        fields = fields,
        typeGetter = typeGetter
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableInterfaceType].
 */
typealias GraphQLInterfaceTypeBlock<T> =
        GraphQLMutableInterfaceType<T>.() -> Unit

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
    val element = GraphQLMutableInterfaceType<T>()
    name?.let { element.name = it }
    element.apply(block)
    return element.copy()
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Object Type                |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A type for GraphQL objects.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLObjectType<T : Any> :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives,
    GraphQLElementWithInterfaces<T>,
    GraphQLElementWithFieldDefinitions<T>,
    GraphQLOutputType<T>

/**
 * A mutable variant of [GraphQLObjectType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableObjectType<T : Any> :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLMutableElementWithInterfaces<T>,
    GraphQLMutableElementWithFieldDefinitions<T>,
    GraphQLObjectType<T>

/**
 * Construct a new [GraphQLObjectType] with the given arguments.
 */
fun <T : Any> GraphQLObjectType(
    name: String,
    description: String,
    directives: List<GraphQLDirective>,
    interfaces: List<GraphQLInterfaceType<in T>>,
    onGetBlocks: List<GraphQLGetterBlock<T, *>>,
    onGetBlockingBlocks: List<GraphQLGetterBlockingBlock<T, *>>,
    fields: List<GraphQLFieldDefinition<T, *>>
): GraphQLObjectType<T> {
    return object : GraphQLObjectType<T> {
        override val name = name
        override val description = description
        override val directives = directives
        override val interfaces = interfaces
        override val onGetBlocks = onGetBlocks
        override val onGetBlockingBlocks = onGetBlockingBlocks
        override val fields = fields
    }
}

/**
 * Obtain a new [GraphQLMutableObjectType].
 *
 * @since 2.0.0
 */
fun <T : Any> GraphQLMutableObjectType(): GraphQLMutableObjectType<T> {
    return object : GraphQLMutableObjectType<T> {
        override lateinit var name: String
        override var description: String = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override val interfaces = mutableListOf<GraphQLInterfaceType<in T>>()
        override val onGetBlocks = mutableListOf<GraphQLGetterBlock<T, *>>()
        override val onGetBlockingBlocks = mutableListOf<GraphQLGetterBlockingBlock<T, *>>()
        override val fields = mutableListOf<GraphQLFieldDefinition<T, *>>()
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun <T : Any> GraphQLObjectType<T>.copy(
    name: String = this.name,
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    interfaces: List<GraphQLInterfaceType<in T>> = this.interfaces,
    onGetBlocks: List<GraphQLGetterBlock<T, *>> = this.onGetBlocks,
    onGetBlockingBlocks: List<GraphQLGetterBlockingBlock<T, *>> = this.onGetBlockingBlocks,
    fields: List<GraphQLFieldDefinition<T, *>> = this.fields
): GraphQLObjectType<T> {
    return GraphQLObjectType(
        name = name,
        description = description,
        directives = directives,
        interfaces = interfaces,
        onGetBlocks = onGetBlocks,
        onGetBlockingBlocks = onGetBlockingBlocks,
        fields = fields
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableObjectType].
 */
typealias GraphQLObjectTypeBlock<T> =
        GraphQLMutableObjectType<T>.() -> Unit

/**
 * Construct a new [GraphQLObjectType] with the
 * given [block].
 *
 * ####
 *
 * When self referencing, you might use `by`:
 *
 * ```
 * val MyObjectType: GraphQLObjectType<T> by GraphQLObjectType {
 *      // ...
 * }
 * ```
 *
 * @param name the initial name.
 * @param block the builder block.
 * @return a new object type.
 * @since 2.0.0
 */
fun <T : Any> GraphQLObjectType(
    name: String? = null,
    block: GraphQLObjectTypeBlock<T> = {}
): GraphQLObjectType<T> {
    val element = GraphQLMutableObjectType<T>()
    name?.let { element.name = it }
    element.apply(block)
    return element.copy()
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Scalar Type                |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An interface for scalar types.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLScalarType<T : Any> :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives,
    GraphQLInputOutputType<T> {

    /**
     * The encoder of this scalar.
     */
    val encoder: GraphQLScalarEncoder<T>

    /**
     * The decoder of this scalar.
     */
    val decoder: GraphQLScalarDecoder<T>
}

/**
 * A mutable variant of [GraphQLScalarType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableScalarType<T : Any> :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLScalarType<T> {

    override /* lateinit */ var encoder: GraphQLScalarEncoder<T>
    override /* lateinit */ var decoder: GraphQLScalarDecoder<T>
}

/**
 * Construct a new [GraphQLScalarType] with the given arguments.
 */
fun <T : Any> GraphQLScalarType(
    name: String,
    description: String,
    directives: List<GraphQLDirective>,
    encoder: GraphQLScalarEncoder<T>,
    decoder: GraphQLScalarDecoder<T>
): GraphQLScalarType<T> {
    return object : GraphQLScalarType<T> {
        override val name = name
        override val description = description
        override val directives = directives
        override val encoder = encoder
        override val decoder = decoder
    }
}

/**
 * Obtain a new [GraphQLMutableScalarType].
 *
 * @since 2.0.0
 */
fun <T : Any> GraphQLMutableScalarType(): GraphQLMutableScalarType<T> {
    return object : GraphQLMutableScalarType<T> {
        override lateinit var name: String
        override var description: String = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override lateinit var encoder: GraphQLScalarEncoder<T>
        override lateinit var decoder: GraphQLScalarDecoder<T>
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun <T : Any> GraphQLScalarType<T>.copy(
    name: String = this.name,
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    encoder: GraphQLScalarEncoder<T> = this.encoder,
    decoder: GraphQLScalarDecoder<T> = this.decoder
): GraphQLScalarType<T> {
    return GraphQLScalarType(
        name = name,
        description = description,
        directives = directives,
        encoder = encoder,
        decoder = decoder
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableScalarType].
 */
typealias GraphQLScalarTypeBlock<T> =
        GraphQLMutableScalarType<T>.() -> Unit

/**
 * Construct a new [GraphQLScalarType] with the
 * given [block].
 *
 * @param name the initial name.
 * @param block the builder block.
 * @return a new scalar type.
 * @since 2.0.0
 */
fun <T : Any> GraphQLScalarType(
    name: String? = null,
    block: GraphQLScalarTypeBlock<T> = {}
): GraphQLScalarType<T> {
    val element = GraphQLMutableScalarType<T>()
    name?.let { element.name = it }
    element.apply(block)
    return element.copy()
}

/* ---------------------------------------------- */

/**
 * Set the encoder to the given [encoder].
 *
 * This will replace the current encoder.
 */
fun <T : Any> GraphQLMutableScalarType<T>.encode(
    encoder: GraphQLScalarEncoder<T>
) {
    this.encoder = encoder
}

/**
 * Set the decoder to the given [decoder].
 *
 * This will replace the current decoder.
 */
fun <T : Any> GraphQLMutableScalarType<T>.decode(
    decoder: GraphQLScalarDecoder<T>
) {
    this.decoder = decoder
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Schema                     |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A type for GraphQL schemas.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLSchema :
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives {
    /**
     * The schema's query type.
     *
     * @since 2.0.0
     */
    val query: GraphQLObjectType<Unit>?

    /**
     * The schema's mutation type.
     *
     * @since 2.0.0
     */
    val mutation: GraphQLObjectType<Unit>?

    /**
     * The schema's subscription type.
     *
     * @since 2.0.0
     */
    val subscription: GraphQLObjectType<Unit>?

    /**
     * Additional types.
     *
     * @since 2.0.0
     */
    val additionalTypes: Set<GraphQLType<*>>

    /**
     * Additional directive definitions.
     *
     * @since 2.0.0
     */
    val additionalDirectives: Set<GraphQLDirectiveDefinition>
}

/**
 * A mutable variant of [GraphQLSchema].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableSchema :
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLSchema {
    override var query: GraphQLMutableObjectType<Unit>? /* = null */
    override var mutation: GraphQLMutableObjectType<Unit>? /* = null */
    override var subscription: GraphQLMutableObjectType<Unit>? /* = null */
    override val additionalTypes: MutableSet<GraphQLType<*>> /* = mutableSetOf() */
    override var additionalDirectives: MutableSet<GraphQLDirectiveDefinition> /* = mutableSetOf() */
}

/**
 * Construct a new [GraphQLSchema] with the given arguments.
 */
fun GraphQLSchema(
    description: String,
    directives: List<GraphQLDirective>,
    query: GraphQLObjectType<Unit>?,
    mutation: GraphQLObjectType<Unit>?,
    subscription: GraphQLObjectType<Unit>?,
    additionalTypes: Set<GraphQLType<*>>,
    additionalDirectives: Set<GraphQLDirectiveDefinition>
): GraphQLSchema {
    return object : GraphQLSchema {
        override val description = description
        override val directives = directives
        override val query = query
        override val mutation = mutation
        override val subscription = subscription
        override val additionalTypes = additionalTypes
        override val additionalDirectives = additionalDirectives
    }
}

/**
 * Obtain a new [GraphQLMutableSchema].
 *
 * @since 2.0.0
 */
fun GraphQLMutableSchema(): GraphQLMutableSchema {
    return object : GraphQLMutableSchema {
        override var description: String = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override var query: GraphQLMutableObjectType<Unit>? = null
        override var mutation: GraphQLMutableObjectType<Unit>? = null
        override var subscription: GraphQLMutableObjectType<Unit>? = null
        override val additionalTypes = mutableSetOf<GraphQLType<*>>()
        override var additionalDirectives = mutableSetOf<GraphQLDirectiveDefinition>()
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun GraphQLSchema.copy(
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    query: GraphQLObjectType<Unit>? = this.query,
    mutation: GraphQLObjectType<Unit>? = this.mutation,
    subscription: GraphQLObjectType<Unit>? = this.subscription,
    additionalTypes: Set<GraphQLType<*>> = this.additionalTypes,
    additionalDirectives: Set<GraphQLDirectiveDefinition> = this.additionalDirectives
): GraphQLSchema {
    return GraphQLSchema(
        description = description,
        directives = directives,
        query = query,
        mutation = mutation,
        subscription = subscription,
        additionalTypes = additionalTypes,
        additionalDirectives = additionalDirectives
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableSchema].
 */
typealias GraphQLSchemaBlock =
        GraphQLMutableSchema.() -> Unit

/**
 * Construct a new [GraphQLSchema] with the
 * given [block].
 *
 * @param block the builder block.
 * @return a new schema.
 * @since 2.0.0
 */
fun GraphQLSchema(
    block: GraphQLSchemaBlock = {}
): GraphQLSchema {
    val element = GraphQLMutableSchema()
    element.apply(block)
    return element.copy()
}

/* ---------------------------------------------- */

/**
 * Configure the query of the schema using the
 * given [block].
 */
fun GraphQLMutableSchema.query(
    block: GraphQLObjectTypeBlock<Unit>
) {
    val type = this.query ?: GraphQLMutableObjectType<Unit>()
        .also { it.name = "Query"; this.query = it }

    type.apply(block)
}

/**
 * Configure the mutation of the schema using the
 * given [block].
 */
fun GraphQLMutableSchema.mutation(
    block: GraphQLObjectTypeBlock<Unit>
) {
    val type = this.mutation ?: GraphQLMutableObjectType<Unit>()
        .also { it.name = "Mutation"; this.mutation = it }

    type.apply(block)
}

/**
 * Configure the subscription of the schema using
 * the given [block].
 */
fun GraphQLMutableSchema.subscription(
    block: GraphQLObjectTypeBlock<Unit>
) {
    val type = this.subscription ?: GraphQLMutableObjectType<Unit>()
        .also { it.name = "Subscription"; this.subscription = it }

    type.apply(block)
}

/**
 * Add the given [type].
 */
fun <T> GraphQLMutableSchema.additionalType(
    type: GraphQLType<T>
) {
    this.additionalTypes += type
}

/**
 * Add the given [directive].
 */
fun GraphQLMutableSchema.additionalDirective(
    directive: GraphQLDirectiveDefinition
) {
    this.additionalDirectives += directive
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Type Reference             |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A type reference.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLTypeReference<T> :
    GraphQLElementWithName,
    GraphQLInputOutputType<T>

/**
 * Construct a new [GraphQLTypeReference].
 *
 * @param name the name referencing.
 * @since 2.0.0
 */
fun <T> GraphQLTypeReference(
    name: String
): GraphQLTypeReference<T> {
    return object : GraphQLTypeReference<T> {
        override val name: String = name
    }
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Union Type                 |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A type for GraphQL unions.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLUnionType<T : Any> :
    GraphQLElementWithName,
    GraphQLElementWithDescription,
    GraphQLElementWithDirectives,
    GraphQLElementWithTypeGetter<T>,
    GraphQLOutputType<T> {

    /**
     * The types of the union.
     *
     * @since 2.0.0
     */
    val types: List<GraphQLObjectType<out T>>
}

/**
 * A mutable variant of [GraphQLUnionType].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableUnionType<T : Any> :
    GraphQLMutableElementWithName,
    GraphQLMutableElementWithDescription,
    GraphQLMutableElementWithDirectives,
    GraphQLMutableElementWithTypeGetter<T>,
    GraphQLUnionType<T> {

    override val types: MutableList<GraphQLObjectType<out T>> /* = mutableListOf() */
}

/**
 * Construct a new [GraphQLUnionType] with the given arguments.
 */
fun <T : Any> GraphQLUnionType(
    name: String,
    description: String,
    directives: List<GraphQLDirective>,
    typeGetter: GraphQLTypeGetter<T>,
    types: List<GraphQLObjectType<out T>>
): GraphQLUnionType<T> {
    return object : GraphQLUnionType<T> {
        override val name = name
        override val description = description
        override val directives = directives
        override val typeGetter = typeGetter
        override val types = types
    }
}

/**
 * Obtain a new [GraphQLMutableUnionType].
 *
 * @since 2.0.0
 */
fun <T : Any> GraphQLMutableUnionType(): GraphQLMutableUnionType<T> {
    return object : GraphQLMutableUnionType<T> {
        override lateinit var name: String
        override var description: String = ""
        override val directives = mutableListOf<GraphQLDirective>()
        override var typeGetter: GraphQLTypeGetter<T> = {
            throw NotImplementedError("Type getter of union $name was not implemented.")
        }
        override val types = mutableListOf<GraphQLObjectType<out T>>()
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun <T : Any> GraphQLUnionType<T>.copy(
    name: String = this.name,
    description: String = this.description,
    directives: List<GraphQLDirective> = this.directives,
    typeGetter: GraphQLTypeGetter<T> = this.typeGetter,
    types: List<GraphQLObjectType<out T>> = this.types
): GraphQLUnionType<T> {
    return GraphQLUnionType(
        name = name,
        description = description,
        directives = directives,
        typeGetter = typeGetter,
        types = types
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLMutableUnionType].
 */
typealias GraphQLUnionTypeBlock<T> =
        GraphQLMutableUnionType<T>.() -> Unit

/**
 * Construct a new [GraphQLUnionType] with the
 * given [block].
 *
 * @param types the initial types.
 * @param block the builder block.
 * @return a new union type.
 * @since 2.0.0
 */
fun <T : Any> GraphQLUnionType(
    name: String? = null,
    vararg types: GraphQLObjectType<out T>,
    block: GraphQLUnionTypeBlock<T>
): GraphQLUnionType<T> {
    val builder = GraphQLMutableUnionType<T>()
    name?.let { builder.name = it }
    builder.types += types
    builder.apply(block)
    return builder.copy()
}

/* ---------------------------------------------- */

/**
 * Add the given [type] to the union.
 *
 * @since 2.0.0
 */
fun <T : Any> GraphQLMutableUnionType<T>.type(
    type: GraphQLObjectType<out T>
) {
    types += type
}

/* ============================================== */
