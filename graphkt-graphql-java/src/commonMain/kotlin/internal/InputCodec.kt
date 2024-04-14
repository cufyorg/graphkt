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

import org.cufy.graphkt.schema.GraphQLInputArrayType
import org.cufy.graphkt.schema.GraphQLInputObjectType
import org.cufy.graphkt.schema.GraphQLInputSetter
import org.cufy.graphkt.schema.GraphQLInputType

/*
These functions are responsible for the encoding/decoding
of input object types to type `T` since graphql-java does
not support that.

The encoding/decoding of scalar types are support by graphql-java
thus not implemented here.
*/

//

internal fun <T> GraphQLInputType<T>.furtherDecodeArgumentValue(rawValue: Any?): T {
    if (rawValue == null) {
        @Suppress("UNCHECKED_CAST")
        return null as T
    }
    return when (this) {
        is GraphQLInputObjectType<*> -> {
            @Suppress("UNCHECKED_CAST")
            val type = this as GraphQLInputObjectType<T & Any>

            @Suppress("UNCHECKED_CAST")
            val rawMap = rawValue as? Map<String, Any?>
                ?: error("Expected a map for the value but got $rawValue")

            type.furtherDecodeArgumentValue(rawMap)
        }

        is GraphQLInputArrayType<*> -> {
            @Suppress("UNCHECKED_CAST")
            val type = this as GraphQLInputArrayType<Any?>

            val rawList = rawValue as? List<Any?>
                ?: error("Expected a list for the value but got $rawValue")

            @Suppress("UNCHECKED_CAST")
            type.furtherDecodeArgumentValue(rawList) as T
        }

        else -> {
            @Suppress("UNCHECKED_CAST")
            rawValue as T
        }
    }
}

internal fun <T> GraphQLInputArrayType<T>.furtherDecodeArgumentValue(rawList: List<Any?>): List<T> {
    return rawList.map { type.furtherDecodeArgumentValue(it) }
}

internal fun <T : Any> GraphQLInputObjectType<T>.furtherDecodeArgumentValue(rawMap: Map<String, Any?>): T {
    val instance = this.constructor.invoke()
    this.fields.forEach {
        val name = it.name

        val type = it.type

        @Suppress("UNCHECKED_CAST")
        val setter = it.setter as GraphQLInputSetter<T, Any?>

        val rawValue = rawMap[name]
        val value = type.value.furtherDecodeArgumentValue(rawValue)

        setter(instance, value)
    }
    return instance
}

//

internal fun <T> GraphQLInputType<T>.furtherEncodeArgumentValue(value: T): Any? {
    if (value == null) {
        return null
    }
    return when (this) {
        is GraphQLInputObjectType<*> -> {
            @Suppress("UNCHECKED_CAST")
            val type = this as GraphQLInputObjectType<T & Any>

            type.furtherEncodeArgumentValue(value)
        }

        is GraphQLInputArrayType<*> -> {
            @Suppress("UNCHECKED_CAST")
            val type = this as GraphQLInputArrayType<Any?>

            val list = value as List<Any?>

            type.furtherEncodeArgumentValue(list)
        }

        else -> {
            value
        }
    }
}

internal fun <T> GraphQLInputArrayType<T>.furtherEncodeArgumentValue(list: List<T>): List<Any?> {
    return list.map { type.furtherEncodeArgumentValue(it) }
}

internal fun <T : Any> GraphQLInputObjectType<T>.furtherEncodeArgumentValue(instance: T): Map<String, Any?> {
    val rawMap = mutableMapOf<String, Any?>()
    this.fields.forEach {
        val name = it.name

        @Suppress("UNCHECKED_CAST")
        val type = it.type as GraphQLInputType<Any?>

        val getter = it.getter

        val value = getter(instance)
        val rawValue = type.furtherEncodeArgumentValue(value)

        rawMap[name] = rawValue
    }
    return rawMap
}
