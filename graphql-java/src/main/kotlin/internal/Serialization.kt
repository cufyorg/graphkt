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

import kotlinx.serialization.json.*
import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.*
import java.math.BigDecimal
import java.math.BigInteger

@InternalGraphktApi
fun dynamicEncodeToJsonElement(value: Any?): JsonElement {
    return when (value) {
        null, is GraphQLNull -> JsonNull
        is String -> JsonPrimitive(value)
        is Number -> JsonPrimitive(value)
        is Boolean -> JsonPrimitive(value)
        is GraphQLString -> JsonPrimitive(value.value)
        is GraphQLBoolean -> JsonPrimitive(value.value)
        is GraphQLInteger -> JsonPrimitive(value.value)
        is GraphQLDecimal -> JsonPrimitive(value.value)
        is Map<*, *> -> buildJsonObject {
            value.forEach { key, value ->
                require(key is String) { "Key must be a string" }
                put(key, dynamicEncodeToJsonElement(value))
            }
        }
        is List<*> -> buildJsonArray {
            value.forEach { item ->
                add(dynamicEncodeToJsonElement(item))
            }
        }
        else -> error("Can't dynamically encode: $value")
    }
}

@InternalGraphktApi
fun dynamicDecodeFromJsonElement(value: JsonElement): Any? {
    return when (value) {
        is JsonNull -> null
        is JsonPrimitive -> {
            value.content.takeIf { value.isString }
                ?: value.booleanOrNull
                ?: value.content.toBigIntegerOrNull()
                ?: value.content.toBigDecimalOrNull()
        }
        is JsonObject -> buildMap {
            value.forEach { key, value ->
                put(key, dynamicDecodeFromJsonElement(value))
            }
        }
        is JsonArray -> buildList {
            value.forEach { item ->
                add(dynamicDecodeFromJsonElement(item))
            }
        }
        else -> error("Can't dynamically decode: $value")
    }
}

@InternalGraphktApi
fun dynamicDecodeScalar(value: Any?): GraphQLScalar<*> {
    return when (value) {
        null -> GraphQLNull
        is Boolean -> GraphQLBoolean(value)
        is String -> GraphQLString(value)
        is Int -> GraphQLInteger(value.toBigInteger())
        is Long -> GraphQLInteger(value.toBigInteger())
        is BigInteger -> GraphQLInteger(value)
        is Float -> GraphQLDecimal(value.toBigDecimal())
        is Double -> GraphQLDecimal(value.toBigDecimal())
        is BigDecimal -> GraphQLDecimal(value)
        else -> error("Cannot dynamically decode: ${value.javaClass}")
    }
}
