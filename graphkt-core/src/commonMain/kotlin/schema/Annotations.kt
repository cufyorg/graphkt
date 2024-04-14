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
 * A marker annotation for DSLs.
 *
 * @since 2.0.0
 */
@DslMarker
annotation class GraphQLDsl

/**
 * The scope for any graphql routing.
 */
typealias GraphQLRoute<T> = GraphQLMutableElementWithFieldDefinitions<T>

/**
 * A block of code invoked to fill in options in
 * [GraphQLRoute].
 */
typealias GraphQLRouteBlock<T> = GraphQLRoute<T>.() -> Unit

/**
 * The scope for graphql interface routing.
 */
typealias GraphQLInterfaceRoute<T> = GraphQLMutableInterfaceType<T>

/**
 * A block of code invoked to fill in options in
 * [GraphQLInterfaceRoute].
 */
typealias GraphQLInterfaceRouteBlock<T> = GraphQLInterfaceRoute<T>.() -> Unit

/**
 * The scope for graphql interface routing.
 */
typealias GraphQLObjectRoute<T> = GraphQLMutableObjectType<T>

/**
 * A block of code invoked to fill in options in
 * [GraphQLObjectRoute].
 */
typealias GraphQLObjectRouteBlock<T> = GraphQLObjectRoute<T>.() -> Unit
