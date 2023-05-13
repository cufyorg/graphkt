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
@file:JvmName("Definitions")

package org.cufy.graphkt.schema

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

/*
 Important Note: these interface might change in
 the future. It was made to make it easier to
 implement features for different kids of tweaks
 with less code and not to be used by regular
 users.
*/

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Name                    |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with a name.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithName {
    /**
     * The name of the element.
     */
    val name: String
}

/**
 * A mutable variant of [GraphQLElementWithName].
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithName :
    GraphQLElementWithName {
    override /* lateinit */ var name: String
}

/* ---------------------------------------------- */

/**
 * Set the name to the given [name].
 *
 * This will replace the current name.
 *
 * @since 2.0.0
 */
fun GraphQLMutableElementWithName.name(
    name: String
) {
    this.name = name
}

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Description             |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with a description.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithDescription {
    /**
     * The description of the element.
     */
    val description: String
}

/**
 * A mutable variant of [GraphQLElementWithDescription].
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithDescription :
    GraphQLElementWithDescription {
    override var description: String /* = "" */
}

/* ---------------------------------------------- */

/**
 * Set the description to the given [description].
 *
 * This will replace the current description.
 *
 * @since 2.0.0
 */
fun GraphQLMutableElementWithDescription.description(
    description: String
) {
    this.description = description
}

/**
 * Set the description to result of invoking the
 * given [block].
 *
 * [String.trimIndent] will be used on the returned
 * string.
 *
 * This will replace the current description.
 *
 * @since 2.0.0
 */
fun GraphQLMutableElementWithDescription.description(
    block: () -> String
) {
    this.description = block().trimIndent()
}

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Directives              |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with directives.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithDirectives {
    /**
     * The applied directives.
     *
     * @since 2.0.0
     */
    val directives: List<GraphQLDirective>
}

/**
 * A mutable variant of [GraphQLElementWithDirectives].
 *
 * Important Note: this interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithDirectives :
    GraphQLElementWithDirectives {
    override val directives: MutableList<GraphQLDirective> /* = mutableListOf() */

    /**
     * Add this directive after configuring it
     * with the given [block].
     */
    operator fun GraphQLDirectiveDefinition.invoke(
        block: GraphQLDirectiveBlock = {}
    ) {
        directive(GraphQLDirective(this, block = block))
    }
}

/* ---------------------------------------------- */

/**
 * Apply the given [directive].
 *
 * @since 2.0.0
 */
fun GraphQLMutableElementWithDirectives.directive(
    directive: GraphQLDirective
) {
    directives += directive
}

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Input Type              |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with an input type.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithInputType<T> {
    val type: Lazy<GraphQLInputType<T>>
}

/**
 * A mutable variant of [GraphQLElementWithInputType].
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithInputType<T> :
    GraphQLElementWithInputType<T> {
    /**
     * The current set type.
     */
    override /* lateinit */ var type: Lazy<GraphQLInputType<T>>
}

/* ---------------------------------------------- */

/**
 * Set the type to the result of invoking the given [block].
 *
 * This will replace the current type.
 *
 * @since 2.0.0
 */
fun <T> GraphQLMutableElementWithInputType<T>.type(
    block: () -> GraphQLInputType<T>
) {
    this.type = lazy(block)
}

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Output Type             |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with an output type.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithOutputType<T> {
    val type: Lazy<GraphQLOutputType<T>>
}

/**
 * A mutable variant of [GraphQLElementWithOutputType].
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithOutputType<T> :
    GraphQLElementWithOutputType<T> {
    override /* lateinit */ var type: Lazy<GraphQLOutputType<T>>
}

/* ---------------------------------------------- */

/**
 * Set the type to the result of invoking the given [block].
 *
 * This will replace the current type.
 *
 * @since 2.0.0
 */
fun <T> GraphQLMutableElementWithOutputType<T>.type(
    block: () -> GraphQLOutputType<T>
) {
    this.type = lazy(block)
}

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Arguments               |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with arguments.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithArguments {
    /**
     * The arguments passed to the definition.
     *
     * @since 2.0.0
     */
    val arguments: List<GraphQLArgument<*>>
}

/**
 * A mutable variant of [GraphQLElementWithArguments].
 *
 * Important Note: this interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithArguments :
    GraphQLElementWithArguments {
    override val arguments: MutableList<GraphQLArgument<*>> /* = mutableListOf() */

    /**
     * Add this argument after with the given [value].
     */
    operator fun <T> GraphQLArgumentDefinition<T>.invoke(
        value: T
    ) {
        arguments += GraphQLArgument(this, value)
    }
}

/* ---------------------------------------------- */

/**
 * Add the given [argument].
 *
 * @since 2.0.0
 */
fun <T> GraphQLMutableElementWithArguments.argument(
    argument: GraphQLArgument<T>
) {
    arguments += argument
}

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Argument Definitions    |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with argument definitions.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithArgumentDefinitions {
    /**
     * The arguments this element accepts.
     */
    val arguments: List<GraphQLArgumentDefinition<*>>
}

/**
 * A mutable variant of [GraphQLElementWithArgumentDefinitions].
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithArgumentDefinitions :
    GraphQLElementWithArgumentDefinitions {
    override val arguments: MutableList<GraphQLArgumentDefinition<*>> /* = mutableListOf() */
}

/* ---------------------------------------------- */

/**
 * Add the given argument [definition].
 *
 * @return the added definition.
 * @since 2.0.0
 */
fun <T> GraphQLMutableElementWithArgumentDefinitions.argument(
    definition: GraphQLArgumentDefinition<T>
): GraphQLArgumentDefinition<T> {
    arguments += definition
    return definition
}

/**
 * Add an argument with the given arguments.
 *
 * @param name the initial name.
 * @param type the initial type.
 * @param block the builder block.
 * @return the added definition.
 * @since 2.0.0
 */
fun <T> GraphQLMutableElementWithArgumentDefinitions.argument(
    name: String? = null,
    type: GraphQLInputType<T>? = null,
    block: GraphQLArgumentDefinitionBlock<T> = {}
): GraphQLArgumentDefinition<T> {
    return argument(GraphQLArgumentDefinition(name, type, block))
}

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Field Definitions       |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with field definitions.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithFieldDefinitions<T : Any> {
    /**
     * A block of code to be invoked before every
     * field getter and non-blocking onGet blocks.
     *
     * @since 2.0.0
     */
    val onGetBlocks: List<GraphQLGetterBlock<T, *>>

    /**
     * A blocking block of code to be invoked before
     * every field getter and onGet blocks.
     */
    val onGetBlockingBlocks: List<GraphQLGetterBlockingBlock<T, *>>

    /**
     * The fields of the type.
     *
     * @since 2.0.0
     */
    val fields: List<GraphQLFieldDefinition<T, *>>
}

/**
 * A mutable variant of [GraphQLElementWithFieldDefinitions].
 *
 * Important Note: this interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithFieldDefinitions<T : Any> :
    GraphQLElementWithFieldDefinitions<T> {
    override val onGetBlocks: MutableList<GraphQLGetterBlock<T, *>>
    override val onGetBlockingBlocks: MutableList<GraphQLGetterBlockingBlock<T, *>>
    override val fields: MutableList<GraphQLFieldDefinition<T, *>>
}

/* ---------------------------------------------- */

/**
 * Add the given [block] to be invoked before
 * *every* field getter and non-blocking onGet
 * block.
 */
fun <T : Any> GraphQLMutableElementWithFieldDefinitions<T>.onGet(
    block: GraphQLGetterBlock<T, *>
) {
    onGetBlocks += block
}

/**
 * Add the given [block] to be invoked before
 * *every* field getter and onGet block.
 *
 * Blocking onGet blocks are invoked before
 * non-blocking onGet blocks.
 */
fun <T : Any> GraphQLMutableElementWithFieldDefinitions<T>.onGetBlocking(
    block: GraphQLGetterBlockingBlock<T, *>
) {
    onGetBlockingBlocks += block
}

/**
 * Apply the given field [definition].
 *
 * @since 2.0.0
 */
@GraphQLDsl
fun <T : Any, M> GraphQLMutableElementWithFieldDefinitions<T>.field(
    definition: GraphQLFieldDefinition<T, M>
) {
    fields += definition
}

/**
 * Add a field with the given arguments.
 *
 * @param name the initial name.
 * @param type the initial type.
 * @param block the builder block.
 * @since 2.0.0
 */
@GraphQLDsl
fun <T : Any, M> GraphQLMutableElementWithFieldDefinitions<T>.field(
    name: String? = null,
    type: GraphQLOutputType<M>? = null,
    block: GraphQLFieldDefinitionBlock<T, M> = {}
) {
    field(GraphQLFieldDefinition(name, type, block))
}

/**
 * Add a field with the given arguments.
 *
 * @param property the property.
 * @param type the initial type.
 * @param block the builder block.
 * @since 2.0.0
 */
@GraphQLDsl
fun <T : Any, M> GraphQLMutableElementWithFieldDefinitions<T>.field(
    property: KProperty1<in T, M>,
    type: GraphQLOutputType<M>? = null,
    block: GraphQLFieldDefinitionBlock<T, M> = {}
) {
    field(GraphQLFieldDefinition(property, type, block))
}

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Input Field Definitions |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with input field definitions.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithInputFieldDefinitions<T : Any> {
    /**
     * The fields in this element.
     */
    val fields: List<GraphQLInputFieldDefinition<T, *>>
}

/**
 * A mutable variant of [GraphQLElementWithInputFieldDefinitions].
 *
 * Important Note: this interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithInputFieldDefinitions<T : Any> :
    GraphQLElementWithInputFieldDefinitions<T> {
    override val fields: MutableList<GraphQLInputFieldDefinition<T, *>>
}

/* ---------------------------------------------- */

/**
 * Apply the given field [definition].
 *
 * @since 2.0.0
 */
fun <T : Any, M> GraphQLMutableElementWithInputFieldDefinitions<T>.field(
    definition: GraphQLInputFieldDefinition<T, M>
) {
    fields += definition
}

/**
 * Add a field with the given arguments.
 *
 * @param name the initial name.
 * @param type the initial type.
 * @param block the builder block.
 * @since 2.0.0
 */
fun <T : Any, M> GraphQLMutableElementWithInputFieldDefinitions<T>.field(
    name: String? = null,
    type: GraphQLInputType<M>? = null,
    block: GraphQLInputFieldDefinitionBlock<T, M> = {}
) {
    field(GraphQLInputFieldDefinition(name, type, block))
}

/**
 * Add a field with the given arguments.
 *
 * @param property the property.
 * @param type the initial type.
 * @param block the builder block.
 * @since 2.0.0
 */
fun <T : Any, M> GraphQLMutableElementWithInputFieldDefinitions<T>.field(
    property: KMutableProperty1<in T, M>,
    type: GraphQLInputType<M>? = null,
    block: GraphQLInputFieldDefinitionBlock<T, M> = {}
) {
    field(GraphQLInputFieldDefinition(property, type, block))
}

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Interfaces              |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with interfaces.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithInterfaces<T : Any> {
    /**
     * The interfaces applied to this.
     *
     * @since 2.0.0
     */
    val interfaces: List<GraphQLInterfaceType<in T>>
}

/**
 * A mutable variant of [GraphQLElementWithInterfaces].
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithInterfaces<T : Any> :
    GraphQLElementWithInterfaces<T> {
    override val interfaces: MutableList<GraphQLInterfaceType<in T>>
}

/* ---------------------------------------------- */

/**
 * Add the interface resulted from invoking the
 * given [block].
 */
fun <T : Any> GraphQLMutableElementWithInterfaces<T>.implement(
    block: () -> GraphQLInterfaceType<in T>
) {
    interfaces += block()
}

/* ============================================== */
/* ========|                            |======== */
/* ========| w/ Type Getter             |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An instance with a type getter.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLElementWithTypeGetter<T : Any> {
    val typeGetter: GraphQLTypeGetter<T>
}

/**
 * A mutable variant of [GraphQLElementWithTypeGetter].
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLMutableElementWithTypeGetter<T : Any> :
    GraphQLElementWithTypeGetter<T> {
    override /* lateinit */ var typeGetter: GraphQLTypeGetter<T>
}

/* ---------------------------------------------- */

/**
 * Set the given [getter] to be the type getter.
 *
 * This will override the current getter.
 *
 * @param getter the getter to be set.
 * @since 2.0.0
 */
fun <T : Any> GraphQLMutableElementWithTypeGetter<T>.getType(
    getter: GraphQLTypeGetter<T>
) {
    this.typeGetter = getter
}

/* ============================================== */
