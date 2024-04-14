package org.cufy.graphkt.java.internal

import org.cufy.graphkt.InternalGraphktApi
import org.cufy.graphkt.schema.GraphQLElementWithDirectives
import org.cufy.graphkt.schema.GraphQLElementWithInterfaces
import org.cufy.graphkt.schema.GraphQLInterfaceType

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

/** Collect all the interfaces (recursively) */
fun <T : Any> GraphQLElementWithInterfaces<T>.generateAllInterfacesSequence(): Sequence<GraphQLInterfaceType<in T>> {
    return generateSequence(interfaces) { anInterface ->
        anInterface.flatMap { it.interfaces }.takeIf { it.isNotEmpty() }
    }.flatten().distinct()
}
