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
annotation class MonktDsl

/**
 * The scope for any graphql routing.
 */
typealias GraphQLRoute<T> = WithFieldsBuilder<T>

/**
 * The scope for graphql interface routing.
 */
typealias GraphQLInterfaceRoute<T> = GraphQLInterfaceTypeBuilder<T>

/**
 * The scope for graphql interface routing.
 */
typealias GraphQLObjectRoute<T> = GraphQLObjectTypeBuilder<T>
