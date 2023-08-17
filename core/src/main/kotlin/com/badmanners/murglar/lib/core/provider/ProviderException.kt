package com.badmanners.murglar.lib.core.provider


/**
 * Generic exception for provider related errors.
 */
class ProviderException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}