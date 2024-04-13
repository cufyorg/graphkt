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

/**
 * A convenient class for defining graphql object
 * types when recursion is used very often.
 *
 * This is how to go around recursion in the
 * regular way:
 *
 * ```kotlin
 * val Type1: GraphQLObjectType<Any> = GraphQLObjectType {
 *      // ...
 *
 *      field("second") {
 *          type { Type2 }
 *          // ...
 *      }
 * }
 * val Type2: GraphQLObjectType<Any> = GraphQLObjectType {
 *      // ...
 *
 *      field("first") {
 *          type { Type1 }
 *          // ...
 *      }
 * }
 * ```
 *
 * As seen above, the type of at least one of the
 * fields must be defined explicitly.
 *
 * Another issue is if a direct recursion is needed.
 * The only solution to that is to use the `by`
 * operator:
 *
 * ```kotlin
 * val Type: GraphQLObjectType<Any> by lazy { GraphQLObjectType {
 *      // ...
 *
 *      field("friend") {
 *          type { Type }
 *          // ...
 *      }
 * } }
 * ```
 *
 * So much trouble for so basic feature?
 *
 * Well, what does kotlin have that does not have
 * this issue? `object`s!
 *
 * This abstract class helps you create `object`s
 * that are [GraphQLObjectType]s with the following
 * syntax:
 *
 * ```kotlin
 * object Type1 : GraphQLObjectClass<Any>({
 *      // ...
 *      field("friend") {
 *          type { Type1 }
 *          // ...
 *      }
 * })
 * ```
 *
 * @author LSafer
 * @since 2.0.0
 */
abstract class GraphQLObjectClass<T : Any>(
    block: GraphQLObjectTypeBlock<T>
) : GraphQLObjectType<T> by GraphQLObjectType(null, block)

/**
 * A convenient class for defining graphql interface
 * types when recursion is used very often.
 *
 * > For more detailed explanation see [GraphQLObjectClass].
 *
 * @author LSafer
 * @since 2.0.0
 */
abstract class GraphQLInterfaceClass<T : Any>(
    block: GraphQLInterfaceTypeBlock<T>
) : GraphQLInterfaceType<T> by GraphQLInterfaceType(null, block)

/**
 * A convenient class for defining graphql input object
 * types when recursion is used very often.
 *
 * > For more detailed explanation see [GraphQLObjectClass].
 *
 * @author LSafer
 * @since 2.0.0
 */
abstract class GraphQLInputObjectClass<T : Any>(
    block: GraphQLInputObjectTypeBlock<T>
) : GraphQLInputObjectType<T> by GraphQLInputObjectType(null, null, block)
