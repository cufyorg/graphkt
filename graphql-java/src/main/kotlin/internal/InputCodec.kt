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
import org.cufy.graphkt.schema.GraphQLInputArrayType
import org.cufy.graphkt.schema.GraphQLInputObjectType
import org.cufy.graphkt.schema.GraphQLInputSetter
import org.cufy.graphkt.schema.GraphQLInputType

//

@InternalGraphktApi
fun <T> GraphQLInputType<T>.decode(rawValue: Any?): T {
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

            type.decode(rawMap)
        }
        is GraphQLInputArrayType<*> -> {
            @Suppress("UNCHECKED_CAST")
            val type = this as GraphQLInputArrayType<Any?>

            val rawList = rawValue as? List<Any?>
                ?: error("Expected a list for the value but got $rawValue")

            @Suppress("UNCHECKED_CAST")
            type.decode(rawList) as T
        }
        else -> {
            @Suppress("UNCHECKED_CAST")
            rawValue as T
        }
    }
}

@InternalGraphktApi
fun <T> GraphQLInputArrayType<T>.decode(rawList: List<Any?>): List<T> {
    return rawList.map { type.decode(it) }
}

@InternalGraphktApi
fun <T : Any> GraphQLInputObjectType<T>.decode(rawMap: Map<String, Any?>): T {
    val instance = this.constructor.invoke()
    this.fields.forEach {
        val name = it.name

        val type = it.type

        @Suppress("UNCHECKED_CAST")
        val setter = it.setter as GraphQLInputSetter<T, Any?>

        val rawValue = rawMap[name]
        val value = type.decode(rawValue)

        setter(instance, value)
    }
    return instance
}

//

@InternalGraphktApi
fun <T> GraphQLInputType<T>.encode(value: T): Any? {
    if (value == null) {
        return null
    }
    return when (this) {
        is GraphQLInputObjectType<*> -> {
            @Suppress("UNCHECKED_CAST")
            val type = this as GraphQLInputObjectType<T & Any>

            type.encode(value)
        }
        is GraphQLInputArrayType<*> -> {
            @Suppress("UNCHECKED_CAST")
            val type = this as GraphQLInputArrayType<Any?>

            val list = value as List<Any?>

            type.encode(list)
        }
        else -> {
            value
        }
    }
}

@InternalGraphktApi
fun <T> GraphQLInputArrayType<T>.encode(list: List<T>): List<Any?> {
    return list.map { type.encode(it) }
}

@InternalGraphktApi
fun <T : Any> GraphQLInputObjectType<T>.encode(instance: T): Map<String, Any?> {
    val rawMap = mutableMapOf<String, Any?>()
    this.fields.forEach {
        val name = it.name

        @Suppress("UNCHECKED_CAST")
        val type = it.type as GraphQLInputType<Any?>

        val getter = it.getter

        val value = getter(instance)
        val rawValue = type.encode(value)

        rawMap[name] = rawValue
    }
    return rawMap
}
