package org.cufy.kaguya.internal

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
import kotlin.reflect.full.createType

fun Json.dynamicEncodeToString(value: Any?): String {
    return Json.encodeToString(dynamicEncodeToJsonElement(value))
}

fun Json.dynamicEncodeToJsonElement(value: Any?): JsonElement {
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
        else -> encodeToJsonElement(
            serializersModule.serializer(value::class.createType()),
            value
        )
    }
}
