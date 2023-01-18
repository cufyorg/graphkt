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
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

/*
 Important Note: these interface might change in
 the future. It was made to make it easier to
 implement features for different kids of tweaks
 with less code and not to be used by regular
 users.
*/

/* ===== - WithArgumentDefinitionsBuilder - ===== */

/**
 * An interface for builders with argument definitions.
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
interface WithArgumentDefinitionsBuilder {
    /**
     * The added arguments.
     */
    @AdvancedGraphktApi("Use `argument()` instead")
    val arguments: MutableList<GraphQLArgumentDefinition<*>>
}

/**
 * Add the given argument [definition].
 *
 * @return the added definition.
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun <T> WithArgumentDefinitionsBuilder.argument(
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
fun <T> WithArgumentDefinitionsBuilder.argument(
    name: String? = null,
    type: GraphQLInputType<T>? = null,
    block: GraphQLArgumentDefinitionBuilderBlock<T> = {}
): GraphQLArgumentDefinition<T> {
    return argument(GraphQLArgumentDefinition(name, type, block))
}

/* ========== - WithInputTypeBuilder - ========== */

/**
 * An interface for builders with input types.
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
interface WithInputTypeBuilder<T> {
    /**
     * The current set type.
     */
    @AdvancedGraphktApi("Use `type()` instead")
    var type: Lazy<GraphQLInputType<T>>? // REQUIRED
}

/**
 * Set the type to the result of invoking the given [block].
 *
 * This will replace the current type.
 *
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun <T> WithInputTypeBuilder<T>.type(
    block: () -> GraphQLInputType<T>
) {
    this.type = lazy(block)
}

/* ========= - WithOutputTypeBuilder  - ========= */

/**
 * An interface for builders with output types.
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
interface WithOutputTypeBuilder<T> {
    /**
     * The current set type.
     */
    @AdvancedGraphktApi("Use `type()` instead")
    var type: Lazy<GraphQLOutputType<T>>? // REQUIRED
}

/**
 * Set the type to the result of invoking the given [block].
 *
 * This will replace the current type.
 *
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun <T> WithOutputTypeBuilder<T>.type(
    block: () -> GraphQLOutputType<T>
) {
    this.type = lazy(block)
}

/* ============ - WithNameBuilder  - ============ */

/**
 * An interface for builders with names.
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
interface WithNameBuilder : WithDescriptionBuilder {
    /**
     * The current set name.
     */
    @AdvancedGraphktApi("Use `name()` instead")
    var name: String? // REQUIRED
}

/**
 * Set the name to the given [name].
 *
 * This will replace the current name.
 *
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun WithNameBuilder.name(
    name: String
) {
    this.name = name
}

/* ========= - WithDescriptionBuilder - ========= */

/**
 * An interface for builders with descriptions.
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
interface WithDescriptionBuilder {
    /**
     * The current set description.
     */
    @AdvancedGraphktApi("Use `description()` instead")
    var description: String
}

/**
 * Set the description to result of invoking the
 * given [block].
 *
 * This will replace the current description.
 *
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun WithDescriptionBuilder.description(
    block: () -> String
) {
    this.description = block()
}

/* ========= - WithTypeGetterBuilder  - ========= */

/**
 * An interface for builders with type getters.
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
interface WithTypeGetterBuilder<T : Any> {
    /**
     * The type getter.
     *
     * @since 2.0.0
     */
    @AdvancedGraphktApi("Use `getType()` instead")
    var typeGetter: GraphQLTypeGetter<T>? // REQUIRED
}

/**
 * Set the given [getter] to be the type getter.
 *
 * This will override the current getter.
 *
 * @param getter the getter to be set.
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any> WithTypeGetterBuilder<T>.getType(
    getter: GraphQLTypeGetter<T>
) {
    this.typeGetter = getter
}

/* ========= - WithDirectivesBuilder  - ========= */

/**
 * An interface for builders with directives.
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
interface WithDirectivesBuilder {
    /**
     * The applied directives.
     */
    @AdvancedGraphktApi("Use `directive()` instead")
    val directives: MutableList<GraphQLDirective>

    /**
     * Add this directive after configuring it
     * with the given [block].
     */
    operator fun GraphQLDirectiveDefinition.invoke(
        block: GraphQLDirectiveBuilderBlock = {}
    ) {
        directive(GraphQLDirective(this, block = block))
    }
}

/**
 * Apply the given [directive].
 *
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun WithDirectivesBuilder.directive(
    directive: GraphQLDirective
) {
    directives += directive
}

/* ========== - WithArgumentsBuilder - ========== */

/**
 * An interface for builders with arguments.
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
interface WithArgumentsBuilder {
    /**
     * The added arguments.
     *
     * @since 2.0.0
     */
    @AdvancedGraphktApi("Use `argument()` instead.")
    val arguments: MutableList<GraphQLArgument<*>>

    /**
     * Add this argument after with the given [value].
     */
    @OptIn(AdvancedGraphktApi::class)
    operator fun <T> GraphQLArgumentDefinition<T>.invoke(
        value: T
    ) {
        arguments += GraphQLArgument(this, value)
    }
}

/**
 * Add the given [argument].
 *
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun <T> WithArgumentsBuilder.argument(
    argument: GraphQLArgument<T>
) {
    arguments += argument
}

/* =========== - WithFieldsBuilder  - =========== */

/**
 * An interface for builders with fields.
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
interface WithFieldsBuilder<T : Any> {
    /**
     * The added getter blocks.
     */
    @AdvancedGraphktApi("Use `onGet()` instead")
    val getterBlocks: MutableList<GraphQLGetterBlock<T, Any?>>

    /**
     * The added fields.
     */
    @AdvancedGraphktApi("Use `field()` instead")
    val fields: MutableList<GraphQLFieldDefinition<T, *>>
}

/**
 * Add the given [block] to be invoked before
 * *every* field getter.
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any> WithFieldsBuilder<T>.onGet(
    block: GraphQLGetterBlock<T, Any?>
) {
    getterBlocks += block
}

/**
 * Apply the given field [definition].
 *
 * @since 2.0.0
 */
@GraphQLDsl
@OptIn(AdvancedGraphktApi::class)
fun <T : Any, M> WithFieldsBuilder<T>.field(
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
fun <T : Any, M> WithFieldsBuilder<T>.field(
    name: String? = null,
    type: GraphQLOutputType<M>? = null,
    block: GraphQLFieldDefinitionBuilderBlock<T, M> = {}
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
fun <T : Any, M> WithFieldsBuilder<T>.field(
    property: KProperty1<in T, M>,
    type: GraphQLOutputType<M>? = null,
    block: GraphQLFieldDefinitionBuilderBlock<T, M> = {}
) {
    field(GraphQLPropertyFieldDefinition(property, type, block))
}

/* ========= - WithInputFieldsBuilder - ========= */

/**
 * An interface for builders with input fields.
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
interface WithInputFieldsBuilder<T : Any> {
    /**
     * The added fields.
     */
    @AdvancedGraphktApi("Use `field()` instead")
    val fields: MutableList<GraphQLInputFieldDefinition<T, *>>
}

/**
 * Apply the given field [definition].
 *
 * @since 2.0.0
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any, M> WithInputFieldsBuilder<T>.field(
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
fun <T : Any, M> WithInputFieldsBuilder<T>.field(
    name: String? = null,
    type: GraphQLInputType<M>? = null,
    block: GraphQLInputFieldDefinitionBuilderBlock<T, M> = {}
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
fun <T : Any, M> WithInputFieldsBuilder<T>.field(
    property: KMutableProperty1<in T, M>,
    type: GraphQLInputType<M>? = null,
    block: GraphQLInputFieldDefinitionBuilderBlock<T, M> = {}
) {
    field(GraphQLPropertyInputFieldDefinition(property, type, block))
}

/* ========= - WithInterfacesBuilder  - ========= */

/**
 * An interface for builders with interfaces.
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
interface WithInterfacesBuilder<T : Any> {
    /**
     * The added interfaces.
     */
    @AdvancedGraphktApi("Use `implement()` instead")
    val interfaces: MutableList<GraphQLInterfaceType<in T>>
}

/**
 * Add the interface resulted from invoking the
 * given [block].
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any> WithInterfacesBuilder<T>.implement(
    block: () -> GraphQLInterfaceType<in T>
) {
    interfaces += block()
}

/* ========== - WithDeferredBuilder  - ========== */

/**
 * An interface for builders with deferred code.
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
interface WithDeferredBuilder {
    /**
     * Code to be invoked on this builder before
     * building.
     */
    @AdvancedGraphktApi("Use `deferred()` instead")
    val deferred: MutableList<() -> Unit>
}

/**
 * Add the given [block] to be invoked after
 * building.
 */
@AdvancedGraphktApi
fun <B : WithDeferredBuilder> B.deferred(
    block: B.() -> Unit
) {
    deferred.add { block(this) }
}
