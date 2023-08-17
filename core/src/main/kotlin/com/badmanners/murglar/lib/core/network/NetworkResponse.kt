package com.badmanners.murglar.lib.core.network


/**
 * HTTP response abstraction.
 */
data class NetworkResponse<T>(
    val request: NetworkRequest,
    val result: T,
    val headers: List<Pair<String, String>>,
    val statusCode: Int,
    val message: String
) {

    val isSuccessful: Boolean
        get() = statusCode in 200 until 300

    val locationHeaderValue: String?
        get() = headers.filter { "location".equals(it.first, true) }
            .map { it.second }
            .firstOrNull()
}