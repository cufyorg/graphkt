package org.cufy.graphkt.java.internal

import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.WithDirectives

@InternalGraphktApi
fun WithDirectives.javaDeprecationReason(): String? {
    return directives
        .firstOrNull { it.definition.name == "deprecated" }
        ?.arguments
        ?.firstOrNull { it.definition.name == "reason" }
        ?.value as? String
}

@InternalGraphktApi
fun WithDirectives.javaSpecifiedByUrl(): String? {
    return directives
        .firstOrNull { it.definition.name == "specifiedBy" }
        ?.arguments
        ?.firstOrNull { it.definition.name == "url" }
        ?.value as? String
}
