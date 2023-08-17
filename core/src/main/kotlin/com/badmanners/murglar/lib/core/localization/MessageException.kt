package com.badmanners.murglar.lib.core.localization

/**
 * Generic exception, that will be properly handled by the client and message from which will be shown to the user.
 */
class MessageException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}