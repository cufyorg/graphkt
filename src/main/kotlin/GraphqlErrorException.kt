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
package org.cufy.kaguya

import graphql.ErrorClassification
import graphql.GraphqlErrorException
import graphql.language.SourceLocation

/**
 * A kotlin-friendly wrapper over [GraphqlErrorException.Builder].
 *
 * @author LSafer
 * @since 1.0.0
 */
open class GraphqlErrorExceptionBuilder
    : GraphqlErrorException.Builder() {
    var message: String?
        get() = super.message
        set(value) = run { super.message = value }

    var sourceLocations: List<SourceLocation>
        get() = super.sourceLocations
        set(value) = run { super.sourceLocations }

    var path: List<Any?>
        get() = super.path
        set(value) = run { super.path = value }

    var extensions: Map<String, Any?>?
        get() = super.extensions
        set(value) = run { super.extensions = value }

    var errorClassification: ErrorClassification
        get() = super.errorClassification
        set(value) = run { super.errorClassification = value }
}

// Constructors

/**
 * Create a new [GraphqlErrorException] and apply
 * the given [block] to it.
 *
 * @since 1.0.0
 */
fun GraphqlErrorException(
    block: GraphqlErrorExceptionBuilder.() -> Unit = {}
): GraphqlErrorException {
    val builder = GraphqlErrorExceptionBuilder()
    builder.apply(block)
    return builder.build()
}
