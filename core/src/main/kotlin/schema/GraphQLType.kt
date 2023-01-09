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

/* ============= ------------------ ============= */

/**
 * An interface for all kinds of graphql types.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLType<T>

/**
 * An interface for input types.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputType<T> : GraphQLType<T>

/**
 * An interface for output types.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLOutputType<T> : GraphQLType<T>

/**
 * An interface for types that can be input and
 * output types.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputOutputType<T> :
    GraphQLInputType<T>,
    GraphQLOutputType<T>

/* ============= ------------------ ============= */