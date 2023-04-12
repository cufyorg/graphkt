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

import org.cufy.graphkt.AdvancedGraphktApi
import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.internal.GraphQLDirectiveBuilderImpl
import org.cufy.graphkt.internal.GraphQLDirectiveDefinitionBuilderImpl
import org.cufy.graphkt.internal.GraphQLDirectiveImpl

/* ============= ------------------ ============= */

/**
 * A definition of a graphql directive.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLDirectiveDefinition : WithName, WithArgumentDefinitions {
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
 * An enumeration of the locations a directive can
 * be applied to.
 *
 * @author LSafer
 * @since 2.0.0
 */
enum class GraphQLDirectiveLocation {
    // OPERATION

    QUERY,
    MUTATION,
    SUBSCRIPTION,
    FIELD,
    FRAGMENT_DEFINITION,
    FRAGMENT_SPREAD,
    INLINE_FRAGMENT,
    VARIABLE_DEFINITION,

    // SCHEMA

    SCHEMA,
    SCALAR,
    OBJECT,
    FIELD_DEFINITION,
    ARGUMENT_DEFINITION,
    INTERFACE,
    UNION,
    ENUM,
    ENUM_VALUE,
    INPUT_OBJECT,
    INPUT_FIELD_DEFINITION
}

/**
 * A block of code invoked to fill in options in
 * [GraphQLDirectiveDefinitionBuilder].
 */
typealias GraphQLDirectiveDefinitionBlock =
        GraphQLDirectiveDefinitionBuilder.() -> Unit

/**
 * A block of code invoked to fill in options in
 * [GraphQLDirectiveDefinitionBuilder].
 */
@Deprecated("Use shorter name instead", ReplaceWith(
    "GraphQLDirectiveDefinitionBlock",
    "org.cufy.graphkt.schema.GraphQLDirectiveDefinitionBlock"
))
typealias GraphQLDirectiveDefinitionBuilderBlock =
        GraphQLDirectiveDefinitionBuilder.() -> Unit

/**
 * A builder for creating a [GraphQLDirectiveDefinition].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLDirectiveDefinitionBuilder :
    WithNameBuilder,
    WithArgumentDefinitionsBuilder,
    WithDeferredBuilder {

    /**
     * True, if the directive is repeatable.
     */
    @AdvancedGraphktApi("Use `repeatable()` instead")
    var repeatable: Boolean /* = false */

    /**
     * The locations the directive can be placed at.
     */
    @AdvancedGraphktApi("Use `location()` instead")
    val locations: MutableSet<GraphQLDirectiveLocation>

    /**
     * Build the definition.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLDirectiveDefinition
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLDirectiveDefinitionBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun GraphQLDirectiveDefinitionBuilder(): GraphQLDirectiveDefinitionBuilder {
    return GraphQLDirectiveDefinitionBuilderImpl()
}

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
    val builder = GraphQLDirectiveDefinitionBuilder()
    name?.let { builder.name(it) }
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */

// locations

/**
 * Add the given [location].
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLDirectiveDefinitionBuilder.location(
    location: GraphQLDirectiveLocation
) {
    locations += location
}

// repeatable

/**
 * Set the repeatable property to the given [value].
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLDirectiveDefinitionBuilder.repeatable(
    value: Boolean = true
) {
    repeatable = value
}

/* ============= ------------------ ============= */

/**
 * An instance of some directive.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLDirective : WithArguments {
    /**
     * The definition of the directive.
     *
     * @since 2.0.0
     */
    val definition: GraphQLDirectiveDefinition
}

/**
 * A block of code invoked to fill in options in
 * [GraphQLDirectiveBuilder].
 */
typealias GraphQLDirectiveBlock =
        GraphQLDirectiveBuilder.() -> Unit

/**
 * A block of code invoked to fill in options in
 * [GraphQLDirectiveBuilder].
 */
@Deprecated("Use shorter name instead", ReplaceWith(
    "GraphQLDirectiveBlock",
    "org.cufy.graphkt.schema.GraphQLDirectiveBlock"
))
typealias GraphQLDirectiveBuilderBlock =
        GraphQLDirectiveBuilder.() -> Unit

/**
 * A builder for creating a [GraphQLDirective].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLDirectiveBuilder :
    WithArgumentsBuilder,
    WithDeferredBuilder {

    /**
     * The definition.
     */
    @AdvancedGraphktApi
    var definition: GraphQLDirectiveDefinition? // REQUIRED

    /**
     * Build the directive.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLDirective
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLDirectiveBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun GraphQLDirectiveBuilder(): GraphQLDirectiveBuilder {
    return GraphQLDirectiveBuilderImpl()
}

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
@OptIn(AdvancedGraphktApi::class)
fun GraphQLDirective(
    definition: GraphQLDirectiveDefinition? = null,
    vararg arguments: GraphQLArgument<*>,
    block: GraphQLDirectiveBlock
): GraphQLDirective {
    val builder = GraphQLDirectiveBuilder()
    definition?.let { builder.definition(it) }
    builder.arguments += arguments
    builder.apply(block)
    return builder.build()
}

/**
 * Construct a new [GraphQLDirective].
 *
 * @param definition the directive definition.
 * @param arguments the arguments.
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun GraphQLDirective(
    definition: GraphQLDirectiveDefinition,
    arguments: List<GraphQLArgument<*>>
): GraphQLDirective {
    return GraphQLDirectiveImpl(
        definition = definition,
        arguments = arguments
    )
}

/* ============= ------------------ ============= */

/**
 * Set the definition to the given [definition].
 *
 * This will replace the current definition.
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLDirectiveBuilder.definition(
    definition: GraphQLDirectiveDefinition
) {
    this.definition = definition
}

/* ============= ------------------ ============= */
