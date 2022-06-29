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
import io.ktor.util.*

/**
 * A kotlin-friendly wrapper over [GraphQLSchema.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLSchemaBuilder :
    GraphQLSchema.Builder()

/**
 * Create a new [GraphQLSchema] and apply the
 * given [block] to it.
 *
 * @since 1.0.0
 */
inline fun GraphQLSchema(
    block: GraphQLSchemaBuilder.() -> Unit = {}
): GraphQLSchema {
    val builder = GraphQLSchemaBuilder()
    builder.apply(block)
    return builder.build()
}

/**
 * Define the query root object type.
 *
 * @since 1.0.0
 */
@KtorDsl
fun GraphQLSchemaBuilder.query(
    name: String = "Query",
    block: GraphQLObjectTypeBuilder<Unit>.() -> Unit = {}
) {
    query(GraphQLObjectType(name, block))
}

/**
 * Define the mutation root object type.
 *
 * @since 1.0.0
 */
@KtorDsl
fun GraphQLSchemaBuilder.mutation(
    name: String = "Mutation",
    block: GraphQLObjectTypeBuilder<Unit>.() -> Unit = {}
) {
    mutation(GraphQLObjectType(name, block))
}

/**
 * Define the subscription root object type.
 *
 * @since 1.0.0
 */
@KtorDsl
fun GraphQLSchemaBuilder.subscription(
    name: String = "Subscription",
    block: GraphQLObjectTypeBuilder<Unit>.() -> Unit = {}
) {
    subscription(GraphQLObjectType(name, block))
}
