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

import graphql.schema.*
import kotlin.experimental.ExperimentalTypeInference

/**
 * A graphql reference is a class to be used
 * as a graphql object type by extending it
 * instead of using [GraphQLObjectTypeBuilder].
 *
 * This is a solution to the recursion problem.
 *
 * Example:
 * ```kotlin
 * class MyClass
 *
 * object MyClassObjectType : GraphQLReference<MyClass>("MyClass", {
 *      description = "MyClass"
 *
 *      // ...
 * })
 * ```
 *
 * @author LSafer
 * @since 1.1.0
 */
@ExperimentalKaguyaApi
open class GraphQLReference<T>(
    name: String? = null,
    block: GraphQLObjectTypeBuilder<T>.() -> Unit = {}
) {
    private val _name: String? = name
    private val _block: GraphQLObjectTypeBuilder<T>.() -> Unit = block

    protected var value: Any? = null

    open fun computeValue(): GraphQLOutputType {
        return when (val value = value) {
            null -> {
                val builder = GraphQLObjectTypeBuilder<T>()
                _name?.let { builder.name = it }
                this.value = builder
                builder.apply(_block)
                val type = builder.build()
                this.value = type
                type
            }
            is GraphQLObjectTypeBuilder<*> -> {
                val reference = GraphQLTypeReference(_name)
                this.value = reference
                reference
            }
            is GraphQLTypeReference -> value
            is GraphQLObjectType -> value
            else -> error("Illegal GraphQLReference State")
        }
    }
}

/**
 * Set the field type to the result of [block]
 *
 * @since 1.0.0
 */
@JvmName("referenceType")
@ExperimentalKaguyaApi
@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun GraphQLFieldDefinitionBuilder<*, *>.type(
    block: () -> GraphQLReference<*>
) {
    type = block().computeValue()
}

/**
 * Return a [GraphQLNonNull] wrapping the given [reference].
 *
 * @since 1.1.0
 */
@ExperimentalKaguyaApi
fun GraphQLNonNull(
    reference: GraphQLReference<*>
) = GraphQLNonNull(reference.computeValue())

/**
 * Return a [GraphQLList] wrapping the given [reference].
 *
 * @since 1.1.0
 */
@ExperimentalKaguyaApi
fun GraphQLList(
    reference: GraphQLReference<*>
) = GraphQLList(reference.computeValue())
