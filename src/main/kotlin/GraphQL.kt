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

import graphql.GraphQL
import graphql.schema.GraphQLSchema

/**
 * A kotlin-friendly wrapper over [GraphQL.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLScope(
    schema: GraphQLSchema,
    /**
     * The wrapped builder.
     *
     * @since 1.0.0
     */
    val builder: GraphQL.Builder = GraphQL.newGraphQL(schema)
)

/**
 * Create a new [GraphQL] and apply the given
 * [block] to it.
 *
 * @since 1.0.0
 */
inline fun GraphQL(
    schema: GraphQLSchema,
    block: GraphQLScope.() -> Unit = {}
): GraphQL {
    return GraphQLScope(schema)
        .apply(block)
        .builder
        .build()
}
