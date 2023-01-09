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
package org.cufy.graphkt

/**
 * Marks the annotated component as internal.
 *
 * @since 2.0.0
 */
@RequiresOptIn(
    message = "This is an internal API and was not designed to be used directly",
    level = RequiresOptIn.Level.ERROR
)
annotation class InternalGraphktApi(
    /**
     * Optionally, the reason why the component was marked with this annotation.
     */
    val reason: String = "This is an internal API and was not designed to be used directly"
)

/**
 * Marks the annotated component as experimental.
 *
 * @since 2.0.0
 */
@RequiresOptIn(
    message = "This is an experimental API and might change at anytime",
    level = RequiresOptIn.Level.WARNING
)
annotation class ExperimentalGraphktApi(
    /**
     * Optionally, the reason why the component was marked with this annotation.
     */
    val reason: String = "This is an experimental API and might change at anytime"
)

/**
 * Marks the annotated component as advanced.
 *
 * Components marked by this annotation are meant
 * to be used in libraries and not application code.
 *
 * This was put to avoid users getter into stuff
 * that needs more reading by accident.
 *
 * @since 2.0.0
 */
@RequiresOptIn(
    message = "This API is meant to be used to extend the functionality of graphkt",
    level = RequiresOptIn.Level.WARNING
)
annotation class AdvancedGraphktApi(
    /**
     * Optionally, the reason why the component was marked with this annotation.
     */
    val reason: String = "This API is meant to be used to extend the functionality of graphkt"
)
