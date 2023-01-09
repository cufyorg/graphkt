package org.cufy.kaguya

/**
 * Marks the annotated component as experimental.
 */
@RequiresOptIn(
    message = "This is an experimental kaguya API.",
    level = RequiresOptIn.Level.WARNING
)
annotation class ExperimentalKaguyaApi
