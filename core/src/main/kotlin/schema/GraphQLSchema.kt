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
import org.cufy.graphkt.internal.GraphQLSchemaBuilderImpl

/* ============= ------------------ ============= */

/**
 * A type for GraphQL schemas.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLSchema : WithDescription, WithDirectives {
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
 * A block of code invoked to fill in options in
 * [GraphQLSchemaBuilder].
 */
typealias GraphQLSchemaBuilderBlock =
        GraphQLSchemaBuilder.() -> Unit

/**
 * A builder for creating a [GraphQLSchema].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLSchemaBuilder :
    WithDescriptionBuilder,
    WithDirectivesBuilder,
    WithDeferredBuilder {

    /**
     * The schema's query type.
     *
     * @since 2.0.0
     */
    @AdvancedGraphktApi("Use `query()` instead")
    val query: GraphQLObjectTypeBuilder<Unit>

    /**
     * The schema's mutation type.
     *
     * @since 2.0.0
     */
    @AdvancedGraphktApi("Use `mutation()` instead")
    val mutation: GraphQLObjectTypeBuilder<Unit>

    /**
     * The schema's subscription type.
     *
     * @since 2.0.0
     */
    @AdvancedGraphktApi("Use `subscription()` instead")
    val subscription: GraphQLObjectTypeBuilder<Unit>

    /**
     * Additional types.
     *
     * @since 2.0.0
     */
    @AdvancedGraphktApi("Use `additionalType()` instead")
    val additionalTypes: MutableSet<GraphQLType<*>>

    /**
     * Additional directive definitions.
     *
     * @since 2.0.0
     */
    @AdvancedGraphktApi("Use `additionalDirective()` instead")
    val additionalDirectives: MutableSet<GraphQLDirectiveDefinition>

    /**
     * Build the schema.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLSchema
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLSchemaBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun GraphQLSchemaBuilder(): GraphQLSchemaBuilder {
    return GraphQLSchemaBuilderImpl()
}

/**
 * Construct a new [GraphQLSchema] with the
 * given [block].
 *
 * @param block the builder block.
 * @return a new schema.
 * @since 2.0.0
 */
fun GraphQLSchema(
    block: GraphQLSchemaBuilderBlock = {}
): GraphQLSchema {
    val builder = GraphQLSchemaBuilder()
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */

// query

/**
 * Configure the query of the schema using the
 * given [block].
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLSchemaBuilder.query(
    block: GraphQLObjectTypeBuilderBlock<Unit>
) {
    query.apply(block)
}

// mutation

/**
 * Configure the mutation of the schema using the
 * given [block].
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLSchemaBuilder.mutation(
    block: GraphQLObjectTypeBuilderBlock<Unit>
) {
    mutation.apply(block)
}

// subscription

/**
 * Configure the subscription of the schema using
 * the given [block].
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLSchemaBuilder.subscription(
    block: GraphQLObjectTypeBuilderBlock<Unit>
) {
    subscription.apply(block)
}

// additionalTypes

/**
 * Add the given [type].
 */
@OptIn(AdvancedGraphktApi::class)
fun <T> GraphQLSchemaBuilder.additionalType(
    type: GraphQLType<T>
) {
    this.additionalTypes += type
}

// additionalDirectives

/**
 * Add the given [directive].
 */
@OptIn(AdvancedGraphktApi::class)
fun GraphQLSchemaBuilder.additionalDirective(
    directive: GraphQLDirectiveDefinition
) {
    this.additionalDirectives += directive
}
