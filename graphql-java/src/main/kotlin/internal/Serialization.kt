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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
import org.cufy.graphkt.InternalGraphktApi
import kotlin.reflect.full.createType

@InternalGraphktApi
@OptIn(ExperimentalSerializationApi::class)
internal val GraphQLJavaJson = Json {
    encodeDefaults = false
    explicitNulls = false
    ignoreUnknownKeys = true
}

@InternalGraphktApi
fun dynamicEncodeToJsonElement(value: Any?): JsonElement {
    return when (value) {
        null -> JsonNull
        is String -> JsonPrimitive(value)
        is Number -> JsonPrimitive(value)
        is Boolean -> JsonPrimitive(value)
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
        else -> GraphQLJavaJson.encodeToJsonElement(
            GraphQLJavaJson.serializersModule.serializer(value::class.createType()),
            value
        )
    }
}

@InternalGraphktApi
fun dynamicDecodeFromJsonElement(value: JsonElement): Any? {
    return when (value) {
        is JsonNull -> null
        is JsonPrimitive -> {
            value.content.takeIf { value.isString }
                ?: value.booleanOrNull
                ?: value.longOrNull
                ?: value.doubleOrNull
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
        else -> GraphQLJavaJson.decodeFromJsonElement(
            GraphQLJavaJson.serializersModule.serializer(value::class.createType()),
            value
        )
    }
}
