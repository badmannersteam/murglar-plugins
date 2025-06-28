package com.badmanners.murglar.lib.core.utils


/**
 * Rate limit configuration.
 */
data class RateLimit(

    /**
     * Domain with the optional path, like 'example.com' or 'example.com/api/endpoint'.
     */
    val url: String,

    /**
     * Typical reasonable requests per second rate for the [url].
     */
    val rps: Float,

    /**
     * Acceptable burst requests count, if supported, 0 otherwise.
     */
    val burst: Int = 0
)