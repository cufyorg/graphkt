package org.cufy.kaguya

import graphql.schema.GraphQLEnumValueDefinition

/**
 * A kotlin-friendly wrapper over [GraphQLEnumValueDefinition.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphQLEnumValueDefinitionScope(
    name: String? = null,
    value: Any? = null,
    /**
     * The wrapped builder.
     *
     * @since 1.0.0
     */
    val builder: GraphQLEnumValueDefinition.Builder =
        GraphQLEnumValueDefinition.newEnumValueDefinition()
            .apply { name?.let { name(it) } }
            .apply { value?.let { value(it) } }
) {
    /**
     * The name of the value.
     *
     * @since 1.0.0
     */
    var name: String
        @Deprecated(
            "builder.name is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = TODO("builder.name is not accessible")
        set(value) = run { builder.name(value) }

    /**
     * The description of the value.
     *
     * @since 1.0.0
     */
    var description: String
        @Deprecated(
            "builder.description is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = TODO("builder.description is not accessible")
        set(value) = run { builder.description(value) }

    /**
     * If deprecated, the deprecation reason of the value.
     *
     * @since 1.0.0
     */
    var deprecationReason: String
        @Deprecated(
            "builder.deprecationReason is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = TODO("builder.deprecationReason is not accessible")
        set(value) = run { builder.deprecationReason(value) }

    /**
     * The runtime value of the value.
     *
     * @since 1.0.0
     */
    var value: String
        @Deprecated(
            "builder.value is not accessible",
            level = DeprecationLevel.ERROR
        )
        get() = TODO("builder.name is not accessible")
        set(value) = run { builder.value(value) }
}

/**
 * Create a new [GraphQLEnumValueDefinition] and
 * apply the given [block] to it.
 *
 * @since 1.0.0
 */
fun GraphQLEnumValueDefinition(
    name: String? = null,
    value: Any? = null,
    block: GraphQLEnumValueDefinitionScope.() -> Unit
): GraphQLEnumValueDefinition {
    return GraphQLEnumValueDefinitionScope(name, value)
        .apply(block)
        .builder
        .build()
}
