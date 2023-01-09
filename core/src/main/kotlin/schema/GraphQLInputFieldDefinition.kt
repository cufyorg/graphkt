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

import org.cufy.graphkt.AdvancedGraphktApi
import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.internal.GraphQLInputFieldDefinitionBuilderImpl
import kotlin.reflect.KMutableProperty1

/* ============= ------------------ ============= */

/**
 * A definition of a field in an input object type.
 *
 * @param T the type of the containing object.
 * @param M the type of the field's value.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputFieldDefinition<T : Any, M> : WithName, WithDirectives {
    /**
     * The field's type.
     *
     * @since 2.0.0
     */
    val type: GraphQLInputType<M>

    /**
     * The field's getter.
     *
     * @since 2.0.0
     */
    val getter: GraphQLInputGetter<T, M>

    /**
     * The field's setter.
     *
     * @since 2.0.0
     */
    val setter: GraphQLInputSetter<T, M>
}


/**
 * An input field setter.
 *
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLInputSetter<T, M> =
            (instance: T, value: M) -> Unit

/**
 * An input field getter.
 *
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLInputGetter<T, M> =
            (instance: T) -> M

/**
 * A block of code invoked to fill in options in
 * [GraphQLInputFieldDefinitionBuilder].
 */
typealias GraphQLInputFieldDefinitionBuilderBlock<T, M> =
        GraphQLInputFieldDefinitionBuilder<T, M>.() -> Unit

/**
 * A builder for creating a [GraphQLInputFieldDefinition].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputFieldDefinitionBuilder<T : Any, M> :
    WithNameBuilder,
    WithInputTypeBuilder<M>,
    WithDirectivesBuilder,
    WithDeferredBuilder {

    /**
     * The value setter.
     */
    @AdvancedGraphktApi("Use `set()` instead")
    var setter: GraphQLInputSetter<T, M>? // REQUIRED

    /**
     * The value getter.
     */
    @AdvancedGraphktApi("Use `get()` instead")
    var getter: GraphQLInputGetter<T, M>? // REQUIRED

    /**
     * Build the definition.
     *
     * This will invoke the deferred code and
     * removes it.
     *
     * @since 2.0.0
     */
    fun build(): GraphQLInputFieldDefinition<T, M>
}

/* ============= ------------------ ============= */

/**
 * Obtain a new [GraphQLInputFieldDefinitionBuilder].
 *
 * @since 2.0.0
 */
@OptIn(InternalGraphktApi::class)
fun <T : Any, M> GraphQLInputFieldDefinitionBuilder(): GraphQLInputFieldDefinitionBuilder<T, M> {
    return GraphQLInputFieldDefinitionBuilderImpl()
}

/**
 * Construct a new [GraphQLInputFieldDefinition] with the
 * given [block].
 *
 * @param name the initial name.
 * @param type the initial type.
 * @param block the builder block.
 * @return a new input field definition.
 * @since 2.0.0
 */
fun <T : Any, M> GraphQLInputFieldDefinition(
    name: String? = null,
    type: GraphQLInputType<M>? = null,
    block: GraphQLInputFieldDefinitionBuilderBlock<T, M> = {}
): GraphQLInputFieldDefinition<T, M> {
    val builder = GraphQLInputFieldDefinitionBuilder<T, M>()
    name?.let { builder.name(it) }
    type?.let { builder.type { it } }
    builder.apply(block)
    return builder.build()
}

/**
 * Construct a new [GraphQLInputFieldDefinition] with
 * the [property] and [block].
 *
 * @param property the property.
 * @param type the initial type.
 * @param block the builder block.
 * @return a new field definition.
 * @since 2.0.0
 */
@Suppress("FunctionName")
fun <T : Any, M> GraphQLPropertyInputFieldDefinition(
    property: KMutableProperty1<T, M>,
    type: GraphQLInputType<M>? = null,
    block: GraphQLInputFieldDefinitionBuilderBlock<T, M> = {}
): GraphQLInputFieldDefinition<T, M> {
    val builder = GraphQLInputFieldDefinitionBuilder<T, M>()
    type?.let { builder.type { it } }
    builder.property(property)
    builder.apply(block)
    return builder.build()
}

/* ============= ------------------ ============= */

//

/**
 * Set the name and setter of the definition from
 * the given [property].
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any, M> GraphQLInputFieldDefinitionBuilder<T, M>.property(
    property: KMutableProperty1<T, M>
) {
    this.name = property.name
    this.getter = property.getter
    this.setter = property.setter
}

// getter

/**
 * Set the getter of this field to the given [getter].
 *
 * This will replace the current getter.
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any, M> GraphQLInputFieldDefinitionBuilder<T, M>.get(
    getter: GraphQLInputGetter<T, M>
) {
    this.getter = getter
}

// setter

/**
 * Set the setter of this field to the given [setter].
 *
 * This will replace the current setter.
 */
@OptIn(AdvancedGraphktApi::class)
fun <T : Any, M> GraphQLInputFieldDefinitionBuilder<T, M>.set(
    setter: GraphQLInputSetter<T, M>
) {
    this.setter = setter
}

/* ============= ------------------ ============= */
