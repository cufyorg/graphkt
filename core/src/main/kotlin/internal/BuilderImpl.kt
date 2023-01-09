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

import org.cufy.graphkt.*
import org.cufy.graphkt.schema.*

@InternalGraphktApi
open class GraphQLInterfaceTypeBuilderImpl<T : Any> :
    GraphQLInterfaceTypeBuilder<T> {
    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val interfaces: MutableList<GraphQLInterfaceType<in T>> = mutableListOf()

    @AdvancedGraphktApi
    override val fields: MutableList<GraphQLFieldDefinition<T, *>> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @AdvancedGraphktApi
    override var typeGetter: GraphQLTypeGetter<T>? = null

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLInterfaceType<T> {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLInterfaceTypeImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            directives = directives.toList(),
            interfaces = interfaces.toList(),
            fields = fields.toList(),
            typeGetter = typeGetter
                ?: error("typeGetter is required but was not provided.")
        )
    }
}

@InternalGraphktApi
open class GraphQLObjectTypeBuilderImpl<T : Any> :
    GraphQLObjectTypeBuilder<T> {
    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override val fields: MutableList<GraphQLFieldDefinition<T, *>> = mutableListOf()

    @AdvancedGraphktApi
    override val interfaces: MutableList<GraphQLInterfaceType<in T>> = mutableListOf()

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLObjectType<T> {
        deferred.forEach { it() }
        deferred.clear()

        if (fields.isEmpty() && interfaces.isEmpty())
            error("\"$name\" must define one or more fields.")

        return GraphQLObjectTypeImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            fields = fields.toList(),
            interfaces = interfaces.toList(),
            directives = directives.toList()
        )
    }
}

@InternalGraphktApi
open class GraphQLFieldDefinitionBuilderImpl<T : Any, M> :
    GraphQLFieldDefinitionBuilder<T, M> {
    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override val arguments: MutableList<GraphQLArgumentDefinition<*>> = mutableListOf()

    @AdvancedGraphktApi
    override val getterBlocks: MutableList<GraphQLGetterBlock<T, M>> = mutableListOf()

    @AdvancedGraphktApi
    override var getter: GraphQLFlowGetter<T, M>? = null

    @AdvancedGraphktApi
    override var type: Lazy<GraphQLOutputType<M>>? = null

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class, InternalGraphktApi::class)
    override fun build(): GraphQLFieldDefinition<T, M> {
        deferred.forEach { it() }
        deferred.clear()

        val getter = getter ?: error("getter is required but was not provided.")
        val getterBlocks = getterBlocks.toList()

        return GraphQLFieldDefinitionImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            lazyType = type
                ?: error("type is required but was not provided."),
            arguments = arguments.toList(),
            directives = directives.toList(),
            getter = {
                getterBlocks.forEach { it() }
                getter()
            }
        )
    }
}

@InternalGraphktApi
open class GraphQLArgumentDefinitionBuilderImpl<T> :
    GraphQLArgumentDefinitionBuilder<T> {
    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override var type: Lazy<GraphQLInputType<T>>? = null

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLArgumentDefinition<T> {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLArgumentDefinitionImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            lazyType = type
                ?: error("type is required but was not provided."),
            directives = directives.toList()
        )
    }
}

@InternalGraphktApi
open class GraphQLDirectiveDefinitionBuilderImpl :
    GraphQLDirectiveDefinitionBuilder {
    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override var repeatable: Boolean = false

    @AdvancedGraphktApi
    override val locations: MutableSet<GraphQLDirectiveLocation> = mutableSetOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @AdvancedGraphktApi
    override val arguments: MutableList<GraphQLArgumentDefinition<*>> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLDirectiveDefinition {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLDirectiveDefinitionImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            repeatable = repeatable,
            locations = locations.toSet(),
            arguments = arguments.toList()
        )
    }
}

@InternalGraphktApi
open class GraphQLEnumValueDefinitionBuilderImpl<T> :
    GraphQLEnumValueDefinitionBuilder<T> {
    @AdvancedGraphktApi
    override var value: Lazy<T>? = null

    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLEnumValueDefinition<T> {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLEnumValueDefinitionImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            directives = directives.toList(),
            lazyValue = value
                ?: error("value is required but was not provided.")
        )
    }
}

@InternalGraphktApi
open class GraphQLEnumTypeBuilderImpl<T> :
    GraphQLEnumTypeBuilder<T> {
    @AdvancedGraphktApi
    override val values: MutableList<GraphQLEnumValueDefinition<T>> = mutableListOf()

    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLEnumType<T> {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLEnumTypeImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            directives = directives.toList(),
            values = values.toList()
        )
    }
}

@InternalGraphktApi
open class GraphQLScalarTypeBuilderImpl<T : Any> :
    GraphQLScalarTypeBuilder<T> {
    @AdvancedGraphktApi
    override var encoder: GraphQLScalarEncoder<T>? = null

    @AdvancedGraphktApi
    override var decoder: GraphQLScalarDecoder<T>? = null

    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLScalarType<T> {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLScalarTypeImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            encoder = encoder
                ?: error("encoder is required but was not provided."),
            decoder = decoder
                ?: error("decoder is required but was not provided."),
            directives = directives.toList()
        )
    }
}

@InternalGraphktApi
open class GraphQLInputFieldDefinitionBuilderImpl<T : Any, M> :
    GraphQLInputFieldDefinitionBuilder<T, M> {
    @AdvancedGraphktApi
    override var setter: GraphQLInputSetter<T, M>? = null

    @AdvancedGraphktApi
    override var getter: GraphQLInputGetter<T, M>? = null

    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override var type: Lazy<GraphQLInputType<M>>? = null

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLInputFieldDefinition<T, M> {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLInputFieldDefinitionImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            lazyType = type
                ?: error("type is required but was not provided."),
            directives = directives.toList(),
            setter = setter
                ?: error("setter is required but was not provided."),
            getter = getter
                ?: error("getter is required but was not provided."),
        )
    }
}

@InternalGraphktApi
open class GraphQLInputObjectTypeBuilderImpl<T : Any> :
    GraphQLInputObjectTypeBuilder<T> {
    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override val fields: MutableList<GraphQLInputFieldDefinition<T, *>> = mutableListOf()

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @AdvancedGraphktApi
    override var constructor: GraphQLInputConstructor<T>? = null

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLInputObjectType<T> {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLInputObjectTypeImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            fields = fields.toList(),
            directives = directives.toList(),
            constructor = constructor
                ?: error("constructor is required but was not provided.")
        )
    }
}

@InternalGraphktApi
open class GraphQLSchemaBuilderImpl :
    GraphQLSchemaBuilder {
    @AdvancedGraphktApi
    override val query: GraphQLObjectTypeBuilder<Unit> = GraphQLObjectTypeBuilder<Unit>()
        .apply { name("Query") }

    @AdvancedGraphktApi
    override val mutation: GraphQLObjectTypeBuilder<Unit> = GraphQLObjectTypeBuilder<Unit>()
        .apply { name("Mutation") }

    @AdvancedGraphktApi
    override val subscription: GraphQLObjectTypeBuilder<Unit> = GraphQLObjectTypeBuilder<Unit>()
        .apply { name("Subscription") }

    @AdvancedGraphktApi
    override val additionalTypes: MutableSet<GraphQLType<*>> = mutableSetOf()

    @AdvancedGraphktApi
    override val additionalDirectives: MutableSet<GraphQLDirectiveDefinition> = mutableSetOf()

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLSchema {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLSchemaImpl(
            description = description,
            query = query.build(),
            mutation = mutation.takeIf { it.fields.isNotEmpty() }?.build(),
            subscription = subscription.takeIf { it.fields.isNotEmpty() }?.build(),
            additionalTypes = additionalTypes.toSet(),
            additionalDirectives = additionalDirectives.toSet(),
            directives = directives.toList()
        )
    }
}

@InternalGraphktApi
open class GraphQLUnionTypeBuilderImpl<T : Any> :
    GraphQLUnionTypeBuilder<T> {
    @AdvancedGraphktApi
    override val types: MutableList<GraphQLObjectType<out T>> = mutableListOf()

    @AdvancedGraphktApi
    override var name: String? = null

    @AdvancedGraphktApi
    override var description: String = ""

    @AdvancedGraphktApi
    override var typeGetter: GraphQLTypeGetter<T>? = null

    @AdvancedGraphktApi
    override val directives: MutableList<GraphQLDirective> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLUnionType<T> {
        deferred.forEach { it() }
        deferred.clear()

        if (types.isEmpty())
            error("\"$name\" must define one or more member types.")

        return GraphQLUnionTypeImpl(
            name = name
                ?: error("name is required but was not provided."),
            description = description,
            directives = directives,
            typeGetter = typeGetter
                ?: error("typeGetter is required but was not provided"),
            types = types
        )
    }
}

@InternalGraphktApi
open class GraphQLDirectiveBuilderImpl :
    GraphQLDirectiveBuilder {
    @AdvancedGraphktApi
    override var definition: GraphQLDirectiveDefinition? = null

    @AdvancedGraphktApi
    override val arguments: MutableList<GraphQLArgument<*>> = mutableListOf()

    @AdvancedGraphktApi
    override val deferred: MutableList<() -> Unit> = mutableListOf()

    @OptIn(AdvancedGraphktApi::class)
    override fun build(): GraphQLDirective {
        deferred.forEach { it() }
        deferred.clear()
        return GraphQLDirective(
            definition = definition
                ?: error("definition is required but was not provided."),
            arguments = arguments.toList()
        )
    }
}
