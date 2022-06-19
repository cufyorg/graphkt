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
package org.cufy.kaguya.reflection

import graphql.schema.GraphQLEnumValueDefinition
import org.cufy.kaguya.GraphQLEnumValueDefinition
import org.cufy.kaguya.GraphQLEnumValueDefinitionScope

/**
 * Create a new enum value definition for to the
 * given [field].
 *
 * @since 1.0.0
 */
fun GraphQLEnumValueDefinition(
    enum: Enum<*>,
    block: GraphQLEnumValueDefinitionScope.() -> Unit = {}
): GraphQLEnumValueDefinition {
    return GraphQLEnumValueDefinition(
        name = enum.name,
        value = enum,
        block = block
    )
}
