package com.badmanners.murglar.lib.core.network


/**
 * Exception for cases, when response conversion with [ResponseConverter] failed,
 * but caller want to know status/message/headers.
 */
class ResponseConversionException(
    val request: NetworkRequest,
    cause: Throwable,
    val headers: List<Pair<String, String>>,
    val statusCode: Int,
    val statusMessage: String
) : RuntimeException("Can't convert: $statusCode, $statusMessage, ${cause.message}", cause)