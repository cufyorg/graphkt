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

import graphql.schema.GraphQLSchema

/**
 * A kotlin-friendly wrapper over [GraphQLSchema.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLSchemaScope(
    /**
     * The wrapped builder.
     *
     * @since 1.0.0
     */
    val builder: GraphQLSchema.Builder =
        GraphQLSchema.newSchema()
) {
    /**
     * Define the query root object type.
     *
     * @since 1.0.0
     */
    fun query(
        name: String = "Query",
        block: GraphQLObjectTypeScope<Unit>.() -> Unit
    ) {
        builder.query(GraphQLObjectType(name, block))
    }

    /**
     * Define the mutation root object type.
     *
     * @since 1.0.0
     */
    fun mutation(
        name: String = "Mutation",
        block: GraphQLObjectTypeScope<Unit>.() -> Unit
    ) {
        builder.mutation(GraphQLObjectType(name, block))
    }

    /**
     * Define the subscription root object type.
     *
     * @since 1.0.0
     */
    fun subscription(
        name: String = "Subscription",
        block: GraphQLObjectTypeScope<Unit>.() -> Unit
    ) {
        builder.subscription(GraphQLObjectType(name, block))
    }
}

/**
 * Create a new [GraphQLSchema] and apply the
 * given [block] to it.
 *
 * @since 1.0.0
 */
inline fun GraphQLSchema(
    block: GraphQLSchemaScope.() -> Unit = {}
): GraphQLSchema {
    return GraphQLSchemaScope()
        .apply(block)
        .builder
        .build()
}
