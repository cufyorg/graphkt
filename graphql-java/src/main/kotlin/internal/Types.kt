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
import org.cufy.graphkt.java.*
import org.cufy.graphkt.schema.*
import graphql.schema.FieldCoordinates as JavaFieldCoordinates
import graphql.schema.GraphQLInputType as JavaGraphQLInputType
import graphql.schema.GraphQLList as JavaGraphQLList
import graphql.schema.GraphQLNamedInputType as JavaGraphQLNamedInputType
import graphql.schema.GraphQLNamedOutputType as JavaGraphQLNamedOutputType
import graphql.schema.GraphQLNonNull as JavaGraphQLNonNull
import graphql.schema.GraphQLOutputType as JavaGraphQLOutputType
import graphql.schema.GraphQLScalarType as JavaGraphQLScalarType
import graphql.schema.GraphQLType as JavaGraphQLType
import graphql.schema.GraphQLTypeReference as JavaGraphQLTypeReference
import graphql.schema.GraphQLUnionType as JavaGraphQLUnionType

/* ============= ------------------ ============= */

@InternalGraphktApi
fun <T> TransformContext.addType(
    type: GraphQLType<T>
): JavaGraphQLType {
    return when (type) {
        is GraphQLOutputType<*> -> addOutputType(type)
        is GraphQLInputType<*> -> addInputType(type)
        else -> error("Unexpected Type $type")
    }
}

/* ============= ------------------ ============= */

/**
 * - [GraphQLObjectType]
 * - [GraphQLInterfaceType]
 * - [GraphQLOutputArrayType]
 * - [GraphQLScalarType]
 * - [GraphQLEnumType]
 * - [GraphQLOutputNullableType]
 */
@InternalGraphktApi
fun <T> TransformContext.addOutputType(
    type: GraphQLOutputType<T>
): JavaGraphQLOutputType {
    if (type is GraphQLOutputNullableType<*>)
        return doAddOutputType(type.type)

    return JavaGraphQLNonNull.nonNull(doAddOutputType(type))
}

@InternalGraphktApi
private fun <T> TransformContext.doAddOutputType(
    type: GraphQLOutputType<T>
): JavaGraphQLOutputType {
    // double-wrapped; first wrap already handled in 'addOutputType'
    if (type is GraphQLOutputNullableType<*>)
        return doAddOutputType(type.type)

    if (type is GraphQLOutputArrayType<*>)
        return JavaGraphQLList.list(doAddOutputType(type.type))

    if (type is GraphQLTypeReference<T>)
        return JavaGraphQLTypeReference.typeRef(type.name)

    return when (type) {
        is GraphQLUnionType<*> -> addUnionType(type)
        is GraphQLEnumType<*> -> addEnumType(type)
        is GraphQLScalarType<*> -> addScalarType(type)
        is GraphQLInterfaceType<*> -> addInterfaceType(type)
        is GraphQLObjectType<*> -> addObjectType(type)
        else -> error("Unexpected Output Type $type")
    }
}

/* ============= ------------------ ============= */

/**
 * - [GraphQLInputArrayType]
 * - [GraphQLScalarType]
 * - [GraphQLEnumType]
 * - [GraphQLInputNullableType]
 * - [GraphQLInputObjectType]
 */
@InternalGraphktApi
fun <T> TransformContext.addInputType(
    type: GraphQLInputType<T>
): JavaGraphQLInputType {
    if (type is GraphQLInputNullableType<*>)
        return doAddInputType(type.type)

    return JavaGraphQLNonNull.nonNull(doAddInputType(type))
}

@InternalGraphktApi
private fun <T> TransformContext.doAddInputType(
    type: GraphQLInputType<T>
): JavaGraphQLInputType {
    // double-wrapped; first wrap already handled in 'addInputType'
    if (type is GraphQLInputNullableType<*>)
        return doAddInputType(type.type)

    if (type is GraphQLInputArrayType<*>)
        return JavaGraphQLList.list(doAddInputType(type.type))

    if (type is GraphQLTypeReference<T>)
        return JavaGraphQLTypeReference.typeRef(type.name)

    return when (type) {
        is GraphQLEnumType<*> -> addEnumType(type)
        is GraphQLScalarType<*> -> addScalarType(type)
        is GraphQLInputObjectType<*> -> addInputObjectType(type)
        else -> error("Unexpected Input Type Type $type")
    }
}

/* ============= ------------------ ============= */
/* ============= ------------------ ============= */
/* ============= ------------------ ============= */

@InternalGraphktApi
@Suppress("UNCHECKED_CAST")
fun <T : Any> TransformContext.addScalarType(
    type: GraphQLScalarType<T>
): JavaGraphQLScalarType {
    if (type in scalarTypes)
        return scalarTypes[type] ?: error("Recursion is not supported for scalar types")

    scalarTypes[type] = null

    //

    val name = type.name
    val description = type.description
    val coercing = JavaCoercing(type)
    val directives = type.directives.map {
        JavaGraphQLAppliedDirective(it)
    }
    val specifiedByUrl = type.directives
        .firstOrNull { it.definition.name == "specifiedBy" }
        ?.arguments?.firstOrNull { it.definition.name == "url" }
        ?.value as? String

    //

    val java = JavaGraphQLScalarType
        .newScalar()
        .name(name)
        .description(description)
        .replaceAppliedDirectives(directives)
        .specifiedByUrl(specifiedByUrl)
        .coercing(coercing)
        .build()

    scalarTypes[type] = java

    return java
}

/* ============= ------------------ ============= */

@InternalGraphktApi
@Suppress("UNCHECKED_CAST")
fun <T, R> TransformContext.addEnumType(
    type: GraphQLEnumType<T>
): R where R : JavaGraphQLNamedInputType, R : JavaGraphQLNamedOutputType {
    if (type in enumTypes)
        return enumTypes[type] as? R ?: JavaGraphQLTypeReference.typeRef(type.name) as R

    enumTypes[type] = null

    //

    val name = type.name
    val description = type.description
    val values = type.values.map {
        JavaGraphQLEnumValueDefinition(it)
    }
    val directives = type.directives.map {
        JavaGraphQLAppliedDirective(it)
    }

    //

    val java = graphql.schema.GraphQLEnumType
        .newEnum()
        .name(name)
        .description(description)
        .replaceValues(values)
        .replaceAppliedDirectives(directives)
        .build()

    //

    enumTypes[type] = java

    return java as R
}

/* ============= ------------------ ============= */

@InternalGraphktApi
fun <T : Any> TransformContext.addInputObjectType(
    type: GraphQLInputObjectType<T>
): JavaGraphQLNamedInputType {
    if (type in inputObjectTypes)
        return inputObjectTypes[type] ?: JavaGraphQLTypeReference.typeRef(type.name)

    inputObjectTypes[type] = null

    //

    val name = type.name
    val description = type.description
    val fields = type.fields.map {
        JavaGraphQLInputObjectField(it)
    }
    val directives = type.directives.map {
        JavaGraphQLAppliedDirective(it)
    }

    //

    val java = graphql.schema.GraphQLInputObjectType.newInputObject()
        .name(name)
        .description(description)
        .replaceAppliedDirectives(directives)
        .replaceFields(fields)
        .build()

    //

    inputObjectTypes[type] = java

    return java
}

/* ============= ------------------ ============= */

@InternalGraphktApi
fun <T : Any> TransformContext.addUnionType(
    type: GraphQLUnionType<T>
): JavaGraphQLNamedOutputType {
    if (type in unionTypes)
        return unionTypes[type] ?: JavaGraphQLTypeReference.typeRef(type.name)

    unionTypes[type] = null

    //

    val name = type.name
    val description = type.description
    val types = type.types.map { addObjectType(it) }
    val directives = type.directives.map {
        JavaGraphQLAppliedDirective(it)
    }

    addTypeGetters(type)

    //

    val java = JavaGraphQLUnionType
        .newUnionType()
        .name(name)
        .description(description)
        .replacePossibleTypes(types)
        .replaceAppliedDirectives(directives)
        .build()

    //

    unionTypes[type] = java

    return java
}

@InternalGraphktApi
private fun <T : Any> TransformContext.addTypeGetters(
    type: GraphQLUnionType<T>
) {
    val typeResolver = with(runtime) { JavaTypeResolver(type.typeGetter) }

    codeRegistry.typeResolver(type.name, typeResolver)
}

/* ============= ------------------ ============= */

@InternalGraphktApi
fun <T : Any> TransformContext.addInterfaceType(
    type: GraphQLInterfaceType<T>
): JavaGraphQLNamedOutputType {
    if (type in interfaceTypes)
        return interfaceTypes[type] ?: JavaGraphQLTypeReference.typeRef(type.name)

    interfaceTypes[type] = null

    //

    val typeResolver = with(runtime) { JavaTypeResolver(type.typeGetter) }

    codeRegistry.typeResolver(type.name, typeResolver)

    //

    val name = type.name
    val description = type.description
    val interfaces = type.interfaces.map {
        addInterfaceType(it)
    }
    val fields = type.fields.map {
        JavaGraphQLFieldDefinition(it)
    }
    val directives = type.directives.map {
        JavaGraphQLAppliedDirective(it)
    }

    //

    val java = graphql.schema.GraphQLInterfaceType
        .newInterface()
        .name(name)
        .description(description)
        .replaceFields(fields)
        .replaceInterfacesOrReferences(interfaces)
        .replaceAppliedDirectives(directives)
        .build()

    //

    interfaceTypes[type] = java

    return java
}

/* ============= ------------------ ============= */

@InternalGraphktApi
fun <T : Any> TransformContext.addObjectType(
    type: GraphQLObjectType<T>
): JavaGraphQLNamedOutputType {
    if (type in objectTypes)
        return objectTypes[type] ?: JavaGraphQLTypeReference.typeRef(type.name)

    objectTypes[type] = null

    //

    val allFields = generateSequence(type.interfaces) {
        it.flatMap { it.interfaces }.takeIf { it.isNotEmpty() }
    }.flatten().toList().flatMap { it.fields } + type.fields

    allFields.forEach {
        @Suppress("UNCHECKED_CAST")
        it as GraphQLFieldDefinition<T, Any?>

        val coordinates = JavaFieldCoordinates.coordinates(type.name, it.name)
        val fetcher = with(runtime) { JavaDataFetcher(it.getter, it) }

        codeRegistry.dataFetcher(coordinates, fetcher)
    }

    //

    val name = type.name
    val description = type.description
    val interfaces = type.interfaces.map {
        addInterfaceType(it)
    }
    val fields = allFields.map {
        JavaGraphQLFieldDefinition(it)
    }
    val directives = type.directives.map {
        JavaGraphQLAppliedDirective(it)
    }

    //

    val java = graphql.schema.GraphQLObjectType
        .newObject()
        .name(name)
        .description(description)
        .replaceFields(fields)
        .replaceInterfaces(interfaces)
        .replaceAppliedDirectives(directives)
        .build()

    //

    objectTypes[type] = java

    return java
}
