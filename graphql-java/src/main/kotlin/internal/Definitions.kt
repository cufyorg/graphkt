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
import graphql.schema.GraphQLDirective as JavaGraphQLDirective
import graphql.schema.GraphQLEnumValueDefinition as JavaGraphQLEnumValueDefinition
import graphql.schema.GraphQLFieldDefinition as JavaGraphQLFieldDefinition
import graphql.schema.GraphQLInputObjectField as JavaGraphQLInputObjectField

/* =========== - ArgumentDefinition - =========== */

@InternalGraphktApi
fun <T> TransformContext.JavaGraphQLArgument(
    definition: GraphQLArgumentDefinition<T>
): JavaGraphQLArgument {
    val name = definition.name
    val description = definition.description
    val type = addInputType(definition.type)
    val directives = definition.directives.map {
        JavaGraphQLAppliedDirective(it)
    }

    //

    return JavaGraphQLArgument
        .newArgument()
        .name(name)
        .description(description)
        .type(type)
        .replaceAppliedDirectives(directives)
        .build()
}

/* ========== - EnumValueDefinition  - ========== */

@InternalGraphktApi
fun <T> TransformContext.JavaGraphQLEnumValueDefinition(
    definition: GraphQLEnumValueDefinition<T>
): JavaGraphQLEnumValueDefinition {
    val name = definition.name
    val description = definition.description
    val value = definition.value
    val directives = definition.directives.map {
        JavaGraphQLAppliedDirective(it)
    }

    //

    return JavaGraphQLEnumValueDefinition
        .newEnumValueDefinition()
        .name(name)
        .description(description)
        .replaceAppliedDirectives(directives)
        .value(value)
        .build()
}

/* ============ - FieldDefinition  - ============ */

@InternalGraphktApi
fun <T : Any, M> TransformContext.JavaGraphQLFieldDefinition(
    definition: GraphQLFieldDefinition<T, M>
): JavaGraphQLFieldDefinition {
    val name = definition.name
    val description = definition.description
    val type = addOutputType(definition.type)
    val arguments = definition.arguments.map {
        JavaGraphQLArgument(it)
    }
    val directives = definition.directives.map {
        JavaGraphQLAppliedDirective(it)
    }

    //

    return JavaGraphQLFieldDefinition
        .newFieldDefinition()
        .name(name)
        .description(description)
        .type(type)
        .replaceArguments(arguments)
        .replaceAppliedDirectives(directives)
        .build()
}

/* ========== - InputFieldDefinition - ========== */

@InternalGraphktApi
fun <T : Any, M> TransformContext.JavaGraphQLInputObjectField(
    definition: GraphQLInputFieldDefinition<T, M>
): JavaGraphQLInputObjectField {
    val name = definition.name
    val description = definition.description
    val type = addInputType(definition.type)
    val directives = definition.directives.map {
        JavaGraphQLAppliedDirective(it)
    }

    //

    return JavaGraphQLInputObjectField
        .newInputObjectField()
        .name(name)
        .description(description)
        .type(type)
        .replaceAppliedDirectives(directives)
        .build()
}

/* ========== - DirectiveDefinition  - ========== */

@InternalGraphktApi
fun TransformContext.JavaGraphQLDirective(
    definition: GraphQLDirectiveDefinition
): JavaGraphQLDirective {
    if (definition in directives)
        error("Duplicate directive: $definition")

    directives[definition] = null

    //

    val name = definition.name
    val description = definition.description
    val repeatable = definition.repeatable
    val locations = definition.locations.map {
        JavaDirectiveLocation(it)
    }
    val arguments = definition.arguments.map {
        JavaGraphQLArgument(it)
    }

    //

    val java = JavaGraphQLDirective.newDirective()
        .name(name)
        .description(description)
        .repeatable(repeatable)
        .apply { locations.forEach { validLocation(it) } }
        .replaceArguments(arguments)
        .build()

    //

    directives[definition] = java

    return java
}
