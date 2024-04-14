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
import graphql.schema.GraphQLArgument as JavaGraphQLArgument
import graphql.schema.GraphQLEnumValueDefinition as JavaGraphQLEnumValueDefinition
import graphql.schema.GraphQLFieldDefinition as JavaGraphQLFieldDefinition
import graphql.schema.GraphQLInputObjectField as JavaGraphQLInputObjectField

/* =========== - ArgumentDefinition - =========== */

@InternalGraphktApi
fun <T> TransformContext.transformGraphQLArgumentDefinition(
    definition: GraphQLArgumentDefinition<T>
): JavaGraphQLArgument {
    val name = definition.name
    val description = definition.description
    val type = addInputType(definition.type.value)
    val directives = definition.directives.map {
        transformGraphQLDirective(it)
    }
    val deprecationReason = definition.obtainDeprecationReason()

    //

    return JavaGraphQLArgument
        .newArgument()
        .name(name)
        .description(description)
        .type(type)
        .replaceAppliedDirectives(directives)
        .deprecate(deprecationReason)
        .build()
}

/* ========== - EnumValueDefinition  - ========== */

@InternalGraphktApi
fun <T> TransformContext.transformGraphQLEnumValueDefinition(
    definition: GraphQLEnumValueDefinition<T>
): JavaGraphQLEnumValueDefinition {
    val name = definition.name
    val description = definition.description
    val value = definition.value
    val directives = definition.directives.map {
        transformGraphQLDirective(it)
    }
    val deprecationReason = definition.obtainDeprecationReason()

    //

    return JavaGraphQLEnumValueDefinition
        .newEnumValueDefinition()
        .name(name)
        .description(description)
        .replaceAppliedDirectives(directives)
        .deprecationReason(deprecationReason)
        .value(value)
        .build()
}

/* ============ - FieldDefinition  - ============ */

@InternalGraphktApi
fun <T : Any, M> TransformContext.transformGraphQLFieldDefinition(
    definition: GraphQLFieldDefinition<T, M>
): JavaGraphQLFieldDefinition {
    val name = definition.name
    val description = definition.description
    val type = addOutputType(definition.type.value)
    val arguments = definition.arguments.map {
        transformGraphQLArgumentDefinition(it)
    }
    val directives = definition.directives.map {
        transformGraphQLDirective(it)
    }
    val deprecationReason = definition.obtainDeprecationReason()

    //

    return JavaGraphQLFieldDefinition
        .newFieldDefinition()
        .name(name)
        .description(description)
        .type(type)
        .replaceArguments(arguments)
        .replaceAppliedDirectives(directives)
        .deprecate(deprecationReason)
        .build()
}

/* ========== - InputFieldDefinition - ========== */

@InternalGraphktApi
fun <T : Any, M> TransformContext.transformGraphQLInputFieldDefinition(
    definition: GraphQLInputFieldDefinition<T, M>
): JavaGraphQLInputObjectField {
    val name = definition.name
    val description = definition.description
    val type = addInputType(definition.type.value)
    val directives = definition.directives.map {
        transformGraphQLDirective(it)
    }
    val deprecationReason = definition.obtainDeprecationReason()

    //

    return JavaGraphQLInputObjectField
        .newInputObjectField()
        .name(name)
        .description(description)
        .type(type)
        .replaceAppliedDirectives(directives)
        .deprecate(deprecationReason)
        .build()
}

/* ========== - DirectiveDefinition  - ========== */
