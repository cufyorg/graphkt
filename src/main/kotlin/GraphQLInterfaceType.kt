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

import graphql.schema.GraphQLInterfaceType
import graphql.schema.GraphQLOutputType

/**
 * A kotlin-friendly wrapper over [GraphQLInterfaceType.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLInterfaceTypeScope<T>(
    name: String? = null,
    /**
     * The wrapped builder.
     *
     * @since 1.0.0
     */
    val builder: GraphQLInterfaceType.Builder =
        GraphQLInterfaceType.newInterface()
) {
    /**
     * The name of the interface.
     *
     * @since 1.0.0
     */
    var name: String
        @Deprecated(
            "builder.name is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = TODO("builder.name is not accessible")
        set(value) = run { builder.name(value) }

    /**
     * The description of the interface.
     *
     * @since 1.0.0
     */
    var description: String
        @Deprecated(
            "builder.description is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = TODO("builder.description is not accessible")
        set(value) = run { builder.description(value) }

    /**
     * Define a field for this interface.
     *
     * > NOTE: the field's `resolve` function will be ignored.
     *
     * @since 1.0.0
     */
    fun <M> field(
        name: String? = null,
        type: GraphQLOutputType? = null,
        block: GraphQLFieldDefinitionScope<T, M>.() -> Unit = {}
    ) {
        builder.field(GraphQLFieldDefinition(name, type, block))
    }
}

/**
 * Create a new [GraphQLInterfaceType] and apply the
 * given [block] to it.
 *
 * @since 1.0.0
 */
fun <T> GraphQLInterfaceType(
    name: String? = null,
    block: GraphQLInterfaceTypeScope<T>.() -> Unit = {}
): GraphQLInterfaceType {
    return GraphQLInterfaceTypeScope<T>(name)
        .apply(block)
        .builder
        .build()
}
