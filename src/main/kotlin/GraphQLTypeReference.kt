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

import graphql.schema.GraphQLNamedType
import graphql.schema.GraphQLTypeReference
import kotlin.experimental.ExperimentalTypeInference

/**
 * Construct a new [GraphQLTypeReference].
 *
 * @param name the name of the type.
 * @since 1.0.0
 */
fun GraphQLTypeReference(
    name: String
): GraphQLTypeReference {
    return GraphQLTypeReference.typeRef(name)
}

/**
 * Construct a new [GraphQLTypeReference].
 *
 * @param block a block to lazily get the actual
 *              type and get its name.
 * @since 1.0.0
 */
@JvmName("GraphQLTypeReference1")
fun GraphQLTypeReference(
    block: () -> GraphQLNamedType
): GraphQLTypeReference {
    val namedType by lazy(block)
    return GraphQLTypeReference {
        namedType.name
    }
}

/**
 * Construct a new [GraphQLTypeReference].
 *
 * @param block a block to lazily get the name of
 *              the type.
 * @since 1.0.0
 */
@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun GraphQLTypeReference(
    block: () -> String
): GraphQLTypeReference {
    val name by lazy(block)
    return object : GraphQLTypeReference("LazyType") {
        override fun getName() = name

        override fun copy() = GraphQLTypeReference { this }
    }
}
