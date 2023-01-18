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
package org.cufy.graphkt.internal

import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.*

/**
 * The default implementation of [GraphQLInterfaceType].
 */
@InternalGraphktApi
open class GraphQLInterfaceTypeImpl<T : Any>(
    override val name: String,
    override val description: String,
    override val fields: List<GraphQLFieldDefinition<T, *>>,
    override val interfaces: List<GraphQLInterfaceType<in T>>,
    override val directives: List<GraphQLDirective>,
    override val typeGetter: GraphQLTypeGetter<T>,
    override val getter: GraphQLGetterBlock<T, Any?>
) : GraphQLInterfaceType<T> {
    override fun toString(): String = "GraphQLInterfaceType($name)"
}

/**
 * The default implementation of [GraphQLUnionType].
 */
@InternalGraphktApi
open class GraphQLUnionTypeImpl<T : Any>(
    override val name: String,
    override val description: String,
    override val types: List<GraphQLObjectType<out T>>,
    override val typeGetter: GraphQLTypeGetter<T>,
    override val directives: List<GraphQLDirective>
) : GraphQLUnionType<T> {
    override fun toString(): String = "GraphQLUnionType($name)"
}

/**
 * The default implementation of [GraphQLObjectType].
 */
@InternalGraphktApi
open class GraphQLObjectTypeImpl<T : Any>(
    override val name: String,
    override val description: String,
    override val fields: List<GraphQLFieldDefinition<T, *>>,
    override val interfaces: List<GraphQLInterfaceType<in T>>,
    override val directives: List<GraphQLDirective>,
    override val getter: GraphQLGetterBlock<T, Any?>
) : GraphQLObjectType<T> {
    override fun toString(): String = "GraphQLObjectType($name)"
}

/**
 * The default implementation of [GraphQLFieldDefinition].
 */
@InternalGraphktApi
open class GraphQLFieldDefinitionImpl<T : Any, M>(
    override val name: String,
    override val description: String,
    override val arguments: List<GraphQLArgumentDefinition<*>>,
    override val directives: List<GraphQLDirective>,
    override val getter: GraphQLFlowGetter<T, M>,
    lazyType: Lazy<GraphQLOutputType<M>>
) : GraphQLFieldDefinition<T, M> {
    override val type: GraphQLOutputType<M> by lazyType

    override fun toString(): String = "GraphQLFieldDefinition($name, $type)"
}

@InternalGraphktApi
open class GraphQLArgumentDefinitionImpl<T>(
    override val name: String,
    override val description: String,
    override val directives: List<GraphQLDirective>,
    lazyType: Lazy<GraphQLInputType<T>>
) : GraphQLArgumentDefinition<T> {
    override val type: GraphQLInputType<T> by lazyType

    override fun toString(): String = "GraphQLArgumentDefinition($name, $type)"
}

@InternalGraphktApi
open class GraphQLDirectiveDefinitionImpl(
    override val name: String,
    override val description: String,
    override val repeatable: Boolean,
    override val locations: Set<GraphQLDirectiveLocation>,
    override val arguments: List<GraphQLArgumentDefinition<*>>
) : GraphQLDirectiveDefinition {
    override fun toString(): String = "GraphQLDirectiveDefinition($name)"
}

@InternalGraphktApi
open class GraphQLEnumValueDefinitionImpl<T>(
    override val name: String,
    override val description: String,
    override val directives: List<GraphQLDirective>,
    lazyValue: Lazy<T>
) : GraphQLEnumValueDefinition<T> {
    override val value: T by lazyValue

    override fun toString(): String = "GraphQLEnumValueDefinition($name, $value)"
}

@InternalGraphktApi
open class GraphQLEnumTypeImpl<T>(
    override val name: String,
    override val description: String,
    override val directives: List<GraphQLDirective>,
    override val values: List<GraphQLEnumValueDefinition<T>>
) : GraphQLEnumType<T> {
    override fun toString(): String = "GraphQLEnumType($name)"
}

@InternalGraphktApi
open class GraphQLScalarTypeImpl<T : Any>(
    override val name: String,
    override val description: String,
    override val directives: List<GraphQLDirective>,
    val decoder: GraphQLScalarDecoder<T>,
    val encoder: GraphQLScalarEncoder<T>,
) : GraphQLScalarType<T> {
    override fun decode(value: GraphQLScalar<*>): T {
        return decoder(value)
    }

    override fun encode(value: T): GraphQLScalar<*> {
        return encoder(value)
    }

    override fun toString(): String = "GraphQLScalarType($name)"
}

@InternalGraphktApi
open class GraphQLInputFieldDefinitionImpl<T : Any, M>(
    override val name: String,
    override val description: String,
    override val directives: List<GraphQLDirective>,
    override val setter: GraphQLInputSetter<T, M>,
    override val getter: GraphQLInputGetter<T, M>,
    lazyType: Lazy<GraphQLInputType<M>>
) : GraphQLInputFieldDefinition<T, M> {
    override val type: GraphQLInputType<M> by lazyType

    override fun toString(): String = "GraphQLInputFieldDefinition($name, $type)"
}

@InternalGraphktApi
open class GraphQLInputObjectTypeImpl<T : Any>(
    override val name: String,
    override val description: String,
    override val fields: List<GraphQLInputFieldDefinition<T, *>>,
    override val directives: List<GraphQLDirective>,
    override val constructor: GraphQLInputConstructor<T>
) : GraphQLInputObjectType<T> {
    override fun toString(): String = "GraphQLInputObjectType($name)"
}

@InternalGraphktApi
open class GraphQLSchemaImpl(
    override val description: String,
    override val query: GraphQLObjectType<Unit>?,
    override val mutation: GraphQLObjectType<Unit>?,
    override val subscription: GraphQLObjectType<Unit>?,
    override val additionalTypes: Set<GraphQLType<*>>,
    override val additionalDirectives: Set<GraphQLDirectiveDefinition>,
    override val directives: List<GraphQLDirective>
) : GraphQLSchema {
    override fun toString(): String = "GraphQLSchema($description)"
}

@InternalGraphktApi
open class GraphQLArgumentImpl<T>(
    override val definition: GraphQLArgumentDefinition<T>,
    override val value: T
) : GraphQLArgument<T> {
    override fun toString(): String = "GraphQLArgument($value, $definition)"
}

@InternalGraphktApi
open class GraphQLDirectiveImpl(
    override val definition: GraphQLDirectiveDefinition,
    override val arguments: List<GraphQLArgument<*>>
) : GraphQLDirective {
    override fun toString(): String = "GraphQLDirective($definition)"
}

@InternalGraphktApi
open class GraphQLTypeReferenceImpl<T>(
    override val name: String
) : GraphQLTypeReference<T>
