package org.cufy.kaguya.internal

import graphql.Scalars.*
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLOutputType
import graphql.schema.GraphQLType
import org.cufy.kaguya.graphql.ExtraScalars.GraphQLLong
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

fun KType.toGraphQLType(): GraphQLType {
    return toGraphQLTypeOrNull()
        ?: error("Type $this doesn't have a graphql built-in type.")
}

fun KType.toGraphQLInputType(): GraphQLInputType =
    toGraphQLType() as GraphQLInputType

fun KType.toGraphQLOutputType(): GraphQLOutputType =
    toGraphQLType() as GraphQLOutputType

fun KType.toGraphQLTypeOrNull(): GraphQLType? {
    return when (this.jvmErasure) {
        String::class -> when {
            isMarkedNullable -> GraphQLString
            else -> GraphQLNonNull(GraphQLString)
        }
        Boolean::class -> when {
            isMarkedNullable -> GraphQLBoolean
            else -> GraphQLNonNull(GraphQLBoolean)
        }
        Int::class -> when {
            isMarkedNullable -> GraphQLInt
            else -> GraphQLNonNull(GraphQLInt)
        }
        Long::class -> when {
            isMarkedNullable -> GraphQLLong
            else -> GraphQLNonNull(GraphQLLong)
        }
        Float::class, Double::class -> when {
            isMarkedNullable -> GraphQLFloat
            else -> GraphQLNonNull(GraphQLFloat)
        }
        else -> null
    }
}

fun KType.toGraphQLInputTypeOrNull(): GraphQLInputType? =
    toGraphQLTypeOrNull()?.let { it as GraphQLInputType }

fun KType.toGraphQLOutputTypeOrNull(): GraphQLOutputType? =
    toGraphQLTypeOrNull()?.let { it as GraphQLOutputType }
