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
package org.cufy.graphkt.java.internal

import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.*
import graphql.Scalars as JavaScalars
import graphql.schema.GraphQLCodeRegistry as JavaGraphQLCodeRegistry
import graphql.schema.GraphQLDirective as JavaGraphQLDirective
import graphql.schema.GraphQLEnumType as JavaGraphQLEnumType
import graphql.schema.GraphQLInputObjectType as JavaGraphQLInputObjectType
import graphql.schema.GraphQLInterfaceType as JavaGraphQLInterfaceType
import graphql.schema.GraphQLObjectType as JavaGraphQLObjectType
import graphql.schema.GraphQLOutputType as JavaGraphQLOutputType
import graphql.schema.GraphQLScalarType as JavaGraphQLScalarType
import graphql.schema.GraphQLUnionType as JavaGraphQLUnionType

@InternalGraphktApi
class TransformContext {
    val codeRegistry: JavaGraphQLCodeRegistry.Builder = JavaGraphQLCodeRegistry.newCodeRegistry()

    //

    val directives: MutableMap<GraphQLDirectiveDefinition, JavaGraphQLDirective?> = mutableMapOf()

    //

    val unionTypes: MutableMap<GraphQLUnionType<*>, JavaGraphQLUnionType?> = mutableMapOf()
    val interfaceTypes: MutableMap<GraphQLInterfaceType<*>, JavaGraphQLInterfaceType?> = mutableMapOf()
    val enumTypes: MutableMap<GraphQLEnumType<*>, JavaGraphQLEnumType?> = mutableMapOf()
    val inputObjectTypes: MutableMap<GraphQLInputObjectType<*>, JavaGraphQLInputObjectType?> = mutableMapOf()
    val objectTypes: MutableMap<GraphQLObjectType<*>, JavaGraphQLObjectType?> = mutableMapOf()
    val scalarTypes: MutableMap<GraphQLScalarType<*>, JavaGraphQLScalarType?> = mutableMapOf(
        GraphQLIntType to JavaScalars.GraphQLInt,
        GraphQLFloatType to JavaScalars.GraphQLFloat,
        GraphQLStringType to JavaScalars.GraphQLString,
        GraphQLBooleanType to JavaScalars.GraphQLBoolean,
        GraphQLIDType to JavaScalars.GraphQLID
    )

    val runtime: TransformRuntimeContext = TransformRuntimeContext()

    fun fillRuntime() {
        runtime.directives = directives.mapValues { it.value!! }
        runtime.unionTypes = unionTypes.mapValues { it.value!! }
        runtime.interfaceTypes = interfaceTypes.mapValues { it.value!! }
        runtime.enumTypes = enumTypes.mapValues { it.value!! }
        runtime.inputObjectTypes = inputObjectTypes.mapValues { it.value!! }
        runtime.objectTypes = objectTypes.mapValues { it.value!! }
        runtime.scalarTypes = scalarTypes.mapValues { it.value!! }
    }
}

@InternalGraphktApi
class TransformRuntimeContext {
    lateinit var directives: Map<GraphQLDirectiveDefinition, JavaGraphQLDirective>
    lateinit var unionTypes: Map<GraphQLUnionType<*>, JavaGraphQLUnionType>
    lateinit var interfaceTypes: Map<GraphQLInterfaceType<*>, graphql.schema.GraphQLInterfaceType>
    lateinit var enumTypes: Map<GraphQLEnumType<*>, graphql.schema.GraphQLEnumType>
    lateinit var inputObjectTypes: Map<GraphQLInputObjectType<*>, graphql.schema.GraphQLInputObjectType>
    lateinit var objectTypes: Map<GraphQLObjectType<*>, graphql.schema.GraphQLObjectType>
    lateinit var scalarTypes: Map<GraphQLScalarType<*>, graphql.schema.GraphQLScalarType>
}

@InternalGraphktApi
fun TransformRuntimeContext.getDirective(name: String): GraphQLDirectiveDefinition {
    return directives.entries.firstOrNull { it.key.name == name }?.key
        ?: error("Directive was not registered: $name")
}

//

@InternalGraphktApi
fun TransformRuntimeContext.getUnionType(type: GraphQLUnionType<*>): JavaGraphQLUnionType {
    return unionTypes[type]
        ?: error("Type was not registered: ${type.name}")
}

@InternalGraphktApi
fun TransformRuntimeContext.getInterfaceType(type: GraphQLInterfaceType<*>): JavaGraphQLInterfaceType {
    return interfaceTypes[type]
        ?: error("Interface was not registered: ${type.name}")
}

@InternalGraphktApi
fun TransformRuntimeContext.getEnumType(type: GraphQLEnumType<*>): JavaGraphQLEnumType {
    return enumTypes[type]
        ?: error("Enum was not registered: ${type.name}")
}

@InternalGraphktApi
fun TransformRuntimeContext.getInputObjectType(type: GraphQLInputObjectType<*>): JavaGraphQLInputObjectType {
    return inputObjectTypes[type]
        ?: error("Type was not registered: ${type.name}")
}

@InternalGraphktApi
fun TransformRuntimeContext.getObjectType(type: GraphQLObjectType<*>): JavaGraphQLObjectType {
    return objectTypes[type]
        ?: error("Type was not registered")
}

@InternalGraphktApi
fun TransformRuntimeContext.getScalarType(type: GraphQLScalarType<*>): JavaGraphQLScalarType {
    return scalarTypes[type]
        ?: error("Type was not registered: $type")
}

@InternalGraphktApi
fun TransformRuntimeContext.getOutputType(type: GraphQLOutputType<*>): JavaGraphQLOutputType {
    return when (type) {
        is GraphQLEnumType<*> -> getEnumType(type)
        is GraphQLScalarType<*> -> getScalarType(type)
        is GraphQLInterfaceType<*> -> getInterfaceType(type)
        is GraphQLObjectType<*> -> getObjectType(type)
        else -> error("Type was not registered: $type")
    }
}
