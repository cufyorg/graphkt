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

import graphql.schema.GraphQLOutputType
import org.cufy.kaguya.GraphQLFieldDefinitionScope
import org.cufy.kaguya.GraphQLObjectTypeScope
import kotlin.reflect.KProperty1

/**
 * Define a field for this object type.
 *
 * @param field a kotlin property to get the value
 *              automatically using reflection.
 * @param block a function to configure the field.
 * @since 1.0.0
 */
fun <T, M> GraphQLObjectTypeScope<T>.field(
    field: KProperty1<T, M>,
    type: GraphQLOutputType? = null,
    block: GraphQLFieldDefinitionScope<T, M>.() -> Unit = {}
) {
    builder.field(GraphQLFieldDefinition(field, type, block))
}
