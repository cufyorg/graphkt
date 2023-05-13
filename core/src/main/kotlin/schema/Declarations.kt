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

import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.math.BigInteger

/* ============================================== */
/* ========|                            |======== */
/* ========| Type                       |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An interface for all kinds of graphql types.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLType<T>

/**
 * An interface for input types.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputType<T> : GraphQLType<T>

/**
 * An interface for output types.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLOutputType<T> : GraphQLType<T>

/**
 * An interface for types that can be input and
 * output types.
 *
 * @param T runtime type mapping.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputOutputType<T> :
    GraphQLInputType<T>,
    GraphQLOutputType<T>

/* ============================================== */
/* ========|                            |======== */
/* ========| Array Type                 |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An array wrapper for some type.
 *
 * Used to make an array type of some graphql type.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 0.2.0
 */
interface GraphQLArrayType<T> : GraphQLType<List<T>> {
    /**
     * The type of the items in the array.
     *
     * @since 2.0.0
     */
    val type: GraphQLType<T>
}

/**
 * An array wrapper for some type.
 *
 * Used to make an array type of some graphql type.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 0.2.0
 */
interface GraphQLInputArrayType<T> :
    GraphQLArrayType<T>,
    GraphQLInputType<List<T>> {
    override val type: GraphQLInputType<T>
}

/**
 * An array wrapper for some type.
 *
 * Used to make an array type of some graphql type.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 0.2.0
 */
interface GraphQLOutputArrayType<T> :
    GraphQLArrayType<T>,
    GraphQLOutputType<List<T>> {
    override val type: GraphQLOutputType<T>
}

/**
 * An array wrapper for some type.
 *
 * Used to make an array type of some graphql type.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 0.2.0
 */
interface GraphQLInputOutputArrayType<T> :
    GraphQLInputArrayType<T>,
    GraphQLOutputArrayType<T>,
    GraphQLInputOutputType<List<T>> {
    override val type: GraphQLInputOutputType<T>
}

/* ---------------------------------------------- */

/**
 * Wrap the given [type] with a [GraphQLArrayType].
 */
fun <T> GraphQLArrayType(type: GraphQLType<T>): GraphQLArrayType<T> {
    return object : GraphQLArrayType<T> {
        override val type = type

        override fun toString(): String = "GraphQLArrayType($type)"
    }
}

/**
 * Wrap the given [type] with a [GraphQLInputArrayType].
 */
@Suppress("FunctionName")
fun <T> GraphQLArrayType(type: GraphQLInputType<T>): GraphQLInputArrayType<T> {
    return object : GraphQLInputArrayType<T> {
        override val type = type

        override fun toString(): String = "GraphQLArrayType($type)"
    }
}

/**
 * Wrap the given [type] with a [GraphQLOutputArrayType].
 */
@Suppress("FunctionName")
fun <T> GraphQLArrayType(type: GraphQLOutputType<T>): GraphQLOutputArrayType<T> {
    return object : GraphQLOutputArrayType<T> {
        override val type = type

        override fun toString(): String = "GraphQLArrayType($type)"
    }
}

/**
 * Wrap the given [type] with a [GraphQLInputOutputArrayType].
 */
@Suppress("FunctionName")
fun <T> GraphQLArrayType(type: GraphQLInputOutputType<T>): GraphQLInputOutputArrayType<T> {
    return object : GraphQLInputOutputArrayType<T> {
        override val type = type

        override fun toString(): String = "GraphQLArrayType($type)"
    }
}

/* ---------------------------------------------- */

/**
 * Wrap the given [type] with a [GraphQLArrayType].
 */
val <T> GraphQLType<T>.Array: GraphQLArrayType<T>
    get() = GraphQLArrayType(this)

/**
 * Wrap the given [type] with a [GraphQLInputArrayType].
 */
val <T> GraphQLInputType<T>.Array: GraphQLInputArrayType<T>
    get() = GraphQLArrayType(this)

/**
 * Wrap the given [type] with a [GraphQLOutputArrayType].
 */
val <T> GraphQLOutputType<T>.Array: GraphQLOutputArrayType<T>
    get() = GraphQLArrayType(this)

/**
 * Wrap the given [type] with a [GraphQLInputOutputArrayType].
 */
val <T> GraphQLInputOutputType<T>.Array: GraphQLInputOutputArrayType<T>
    get() = GraphQLArrayType(this)

/* ============================================== */
/* ========|                            |======== */
/* ========| Nullable Type              |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * A nullability wrapper for some type.
 *
 * Used to make some graphql type nullable.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLNullableType<T> : GraphQLType<T?> {
    /**
     * The wrapped type.
     *
     * @since 2.0.0
     */
    val type: GraphQLType<T>
}

/**
 * A nullability wrapper for some type.
 *
 * Used to make some graphql type nullable.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputNullableType<T> :
    GraphQLNullableType<T>,
    GraphQLInputType<T?> {
    override val type: GraphQLInputType<T>
}

/**
 * A nullability wrapper for some type.
 *
 * Used to make some graphql type nullable.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLOutputNullableType<T> :
    GraphQLNullableType<T>,
    GraphQLOutputType<T?> {
    override val type: GraphQLOutputType<T>
}

/**
 * A nullability wrapper for some type.
 *
 * Used to make some graphql type nullable.
 *
 * @param T the mapped kotlin type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLInputOutputNullableType<T> :
    GraphQLInputNullableType<T>,
    GraphQLOutputNullableType<T>,
    GraphQLInputOutputType<T?> {
    override val type: GraphQLInputOutputType<T>
}

/* ---------------------------------------------- */

/**
 * Wrap the given [type] with a [GraphQLNullableType].
 */
fun <T> GraphQLNullableType(type: GraphQLType<T>): GraphQLNullableType<T> {
    return object : GraphQLNullableType<T> {
        override val type = type

        override fun toString(): String = "GraphQLNullableType($type)"
    }
}

/**
 * Wrap the given [type] with a [GraphQLInputNullableType].
 */
@Suppress("FunctionName")
fun <T> GraphQLNullableType(type: GraphQLInputType<T>): GraphQLInputNullableType<T> {
    return object : GraphQLInputNullableType<T> {
        override val type = type

        override fun toString(): String = "GraphQLNullableType($type)"
    }
}

/**
 * Wrap the given [type] with a [GraphQLOutputNullableType].
 */
@Suppress("FunctionName")
fun <T> GraphQLNullableType(type: GraphQLOutputType<T>): GraphQLOutputNullableType<T> {
    return object : GraphQLOutputNullableType<T> {
        override val type = type

        override fun toString(): String = "GraphQLNullableType($type)"
    }
}

/**
 * Wrap the given [type] with a [GraphQLInputOutputNullableType].
 */
@Suppress("FunctionName")
fun <T> GraphQLNullableType(type: GraphQLInputOutputType<T>): GraphQLInputOutputNullableType<T> {
    return object : GraphQLInputOutputNullableType<T> {
        override val type = type

        override fun toString(): String = "GraphQLNullableType($type)"
    }
}

/* ---------------------------------------------- */

/**
 * Wrap the given [type] with a [GraphQLNullableType].
 */
val <T> GraphQLType<T>.Nullable: GraphQLNullableType<T>
    get() = GraphQLNullableType(this)

/**
 * Wrap the given [type] with a [GraphQLInputNullableType].
 */
val <T> GraphQLInputType<T>.Nullable: GraphQLInputNullableType<T>
    get() = GraphQLNullableType(this)

/**
 * Wrap the given [type] with a [GraphQLOutputNullableType].
 */
val <T> GraphQLOutputType<T>.Nullable: GraphQLOutputNullableType<T>
    get() = GraphQLNullableType(this)

/**
 * Wrap the given [type] with a [GraphQLInputOutputNullableType].
 */
val <T> GraphQLInputOutputType<T>.Nullable: GraphQLInputOutputNullableType<T>
    get() = GraphQLNullableType(this)

/* ============================================== */
/* ========|                            |======== */
/* ========| Scalar                     |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * The type of a graphql scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
sealed interface GraphQLScalar<V> {
    /**
     * The scalar's runtime value.
     */
    val value: V
}

/**
 * The graphql null scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
object GraphQLNull : GraphQLScalar<Any?> {
    override val value: Any? = null
}

/**
 * The graphql string scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLString(
    override val value: String
) : GraphQLScalar<String>

/**
 * The graphql boolean scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLBoolean(
    override val value: Boolean
) : GraphQLScalar<Boolean>

/**
 * The graphql int scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLInteger(
    override val value: BigInteger
) : GraphQLScalar<BigInteger>

/**
 * The graphql float scalar.
 *
 * @author LSafer
 * @since 2.0.0
 */
class GraphQLDecimal(
    override val value: BigDecimal
) : GraphQLScalar<BigDecimal>

/* ============================================== */
/* ========|                            |======== */
/* ========| Directive Location         |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * An enumeration of the locations a directive can
 * be applied to.
 *
 * @author LSafer
 * @since 2.0.0
 */
enum class GraphQLDirectiveLocation {
    // OPERATION

    QUERY,
    MUTATION,
    SUBSCRIPTION,
    FIELD,
    FRAGMENT_DEFINITION,
    FRAGMENT_SPREAD,
    INLINE_FRAGMENT,
    VARIABLE_DEFINITION,

    // SCHEMA

    SCHEMA,
    SCALAR,
    OBJECT,
    FIELD_DEFINITION,
    ARGUMENT_DEFINITION,
    INTERFACE,
    UNION,
    ENUM,
    ENUM_VALUE,
    INPUT_OBJECT,
    INPUT_FIELD_DEFINITION
}

/* ============================================== */
/* ========|                            |======== */
/* ========| Getter Scope               |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * The value getter scope.
 *
 * @param T the instance type.
 * @param M the value type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLGetterScope<T : Any, M> {
    /**
     * The scope's instance.
     */
    val instance: T

    /**
     * The arguments passed to the field.
     */
    val arguments: List<GraphQLArgument<*>>

    /**
     * The directives applied to the field.
     */
    val directives: List<GraphQLDirective>

    /**
     * The context variables that have been
     * provided before the execution of the query.
     */
    val context: Map<Any?, Any?>

    /**
     * The context variables that have been passed
     * by the containing field.
     */
    val supLocal: Map<Any?, Any?>

    /**
     * The local variables. These variables are
     * set within the scope and will die with the
     * scope.
     */
    val local: MutableMap<Any?, Any?>

    /**
     * The context variables to be passed to the
     * contained fields.
     */
    val subLocal: MutableMap<Any?, Any?>

    // TODO change to extensions when multiple contexts are supported

    /**
     * Get the directive with this definition.
     */
    operator fun GraphQLDirectiveDefinition.invoke(): GraphQLDirective? {
        return directives.firstOrNull { it.definition.name == this.name }
    }

    /**
     * Get the value of this argument.
     */
    operator fun <T> GraphQLArgumentDefinition<T>.invoke(): T {
        val argument = arguments.firstOrNull { it.definition.name == this.name }
                ?: error("Argument was not provided: $name")

        @Suppress("UNCHECKED_CAST")
        return argument.value as T
    }

    /**
     * Get the value of this argument mapped with the given [mapper].
     */
    fun <T, U> GraphQLArgumentDefinition<T>.map(mapper: (T) -> U): U {
        return this().let(mapper)
    }

    /**
     * Get the value of this argument mapped with the given [mapper].
     *
     * If the value is null, the mapping is skipped and `null` is returned instead.
     */
    fun <T, U> GraphQLArgumentDefinition<T>.mapNotNull(mapper: (T & Any) -> U): U? {
        return map { it?.let(mapper) }
    }
}

/**
 * A value getter function type. (single)
 *
 * @param T the instance type.
 * @param M the value type.
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLGetter<T, M> =
        suspend GraphQLGetterScope<T, M>.() -> M

/**
 * A value getter function type.
 *
 * @param T the instance type.
 * @param M the value type.
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLFlowGetter<T, M> =
        suspend GraphQLGetterScope<T, M>.() -> Flow<M>

/**
 * A value getter block function type.
 *
 * @param T the instance type.
 * @param M the value type.
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLGetterBlock<T, M> =
        suspend GraphQLGetterScope<T, M>.() -> Unit

/**
 * A value getter block function type.
 *
 * @param T the instance type.
 * @param M the value type.
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLGetterBlockingBlock<T, M> =
        GraphQLGetterScope<T, M>.() -> Unit

/* ============================================== */
/* ========|                            |======== */
/* ========| Type Getter Scope          |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * The type getter scope.
 *
 * @param T the runtime instance type.
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLTypeGetterScope<T : Any> {
    /**
     * The instance.
     */
    val instance: T

    /**
     * The context variables that have been
     * provided before the execution of the query.
     */
    val context: Map<Any?, Any?>
}

/**
 * A type getter function type.
 *
 * @param T the runtime instance type.
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLTypeGetter<T> =
        GraphQLTypeGetterScope<T>.() -> GraphQLObjectType<out T>

/* ============================================== */
/* ========|                            |======== */
/* ========| Codecs                     |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * The scalar decoder function.
 */
typealias GraphQLScalarEncoder<T> = (T) -> GraphQLScalar<*>

/**
 * The scalar encoder function.
 */
typealias GraphQLScalarDecoder<T> = (GraphQLScalar<*>) -> T

/**
 * An input object constructor.
 *
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLInputConstructor<T> =
            () -> T

/**
 * An input field getter.
 *
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLInputGetter<T, M> =
            (instance: T) -> M

/**
 * An input field setter.
 *
 * @author LSafer
 * @since 2.0.0
 */
typealias GraphQLInputSetter<T, M> =
            (instance: T, value: M) -> Unit

/* ============================================== */
