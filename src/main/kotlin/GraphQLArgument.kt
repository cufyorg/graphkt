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

import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLInputType

/**
 * A kotlin-friendly wrapper over [GraphQLArgument.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLArgumentBuilder<T> :
    GraphQLArgument.Builder() {
    /**
     * The name of the argument.
     *
     * @since 1.0.0
     */
    var name: String
        get() = super.name
        set(value) = run { super.name = value }

    /**
     * The description of the argument.
     *
     * @since 1.0.0
     */
    var description: String
        get() = super.description
        set(value) = run { super.description = value }

    /**
     * If deprecated, the deprecation reason of the argument.
     *
     * @since 1.0.0
     */
    var deprecationReason: String
        @Deprecated(
            "builder.deprecationReason is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = error("builder.deprecationReason is not accessible")
        set(value) = run { deprecate(value) }

    /**
     * The type of the argument.
     *
     * @since 1.0.0
     */
    var type: GraphQLInputType
        @Deprecated(
            "builder.type is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = error("builder.type is not accessible")
        set(value) = run { type(value) }
}

/**
 * Create a new [GraphQLArgument] and apply the
 * given [block] to it.
 *
 * @since 1.0.0
 */
inline fun <T> GraphQLArgument(
    name: String? = null,
    type: GraphQLInputType? = null,
    block: GraphQLArgumentBuilder<T>.() -> Unit = {}
): GraphQLArgument {
    val builder = GraphQLArgumentBuilder<T>()
    name?.let { builder.name = it }
    type?.let { builder.type = it }
    builder.apply(block)
    return builder.build()
}
