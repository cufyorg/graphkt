package org.cufy.graphkt.java.internal

import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.GraphQLElementWithDirectives

@InternalGraphktApi
fun GraphQLElementWithDirectives.obtainDeprecationReason(): String? {
    return directives
        .firstOrNull { it.definition.name == "deprecated" }
        ?.arguments
        ?.firstOrNull { it.definition.name == "reason" }
        ?.value as? String
}

@InternalGraphktApi
fun GraphQLElementWithDirectives.obtainSpecifiedByUrl(): String? {
    return directives
        .firstOrNull { it.definition.name == "specifiedBy" }
        ?.arguments
        ?.firstOrNull { it.definition.name == "url" }
        ?.value as? String
}
