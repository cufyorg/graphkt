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
import org.cufy.graphkt.schema.GraphQLArgument
import org.cufy.graphkt.schema.GraphQLDirective
import org.cufy.graphkt.schema.GraphQLGetterScope
import org.cufy.graphkt.schema.GraphQLTypeGetterScope

@InternalGraphktApi
open class GraphQLGetterScopeImpl<T : Any, M>(
    override val instance: T,
    override val arguments: List<GraphQLArgument<*>>,
    override val directives: List<GraphQLDirective>,
    override val context: Map<Any?, Any?>,
    override val supLocal: Map<Any?, Any?>,
    override val subLocal: MutableMap<Any?, Any?>
) : GraphQLGetterScope<T, M> {
    override val local: MutableMap<Any?, Any?> = mutableMapOf()
}

@InternalGraphktApi
open class GraphQLTypeGetterScopeImpl<T : Any>(
    override val instance: T,
    override val context: Map<*, *>
) : GraphQLTypeGetterScope<T>
