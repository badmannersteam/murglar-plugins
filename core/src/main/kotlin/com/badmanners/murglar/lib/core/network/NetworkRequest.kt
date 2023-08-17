package com.badmanners.murglar.lib.core.network

import java.nio.charset.Charset
import kotlin.text.Charsets.ISO_8859_1
import kotlin.text.Charsets.UTF_8


/**
 * HTTP request abstraction.
 */
data class NetworkRequest(
    val method: String,
    val url: String,
    val followRedirects: Boolean,
    val userAgent: String?,
    val body: String?,
    val contentType: ContentType?,
    val headers: Map<String, String>,
    val parameters: Map<String, String>
) {

    val hasUserAgent: Boolean
        get() = !userAgent.isNullOrEmpty()

    val hasBody: Boolean
        get() = !body.isNullOrEmpty()

    fun toBuilder() = Builder(
        url, method, followRedirects, userAgent, body, contentType, headers.toMutableMap(), parameters.toMutableMap()
    )

    data class Builder @JvmOverloads constructor(
        var url: String? = null,
        var method: String = "GET",
        var followRedirects: Boolean = true,
        var userAgent: String? = null,
        var body: String? = null,
        var contentType: ContentType? = null,
        var headers: MutableMap<String, String> = mutableMapOf(),
        var parameters: MutableMap<String, String> = mutableMapOf()
    ) {
        fun method(method: String) = apply { this.method = method }

        fun url(url: String) = apply { this.url = url }

        fun followRedirects(followRedirects: Boolean) = apply { this.followRedirects = followRedirects }

        fun userAgent(userAgent: String?) = apply { this.userAgent = userAgent }

        fun body(body: String, contentType: ContentType = APPLICATION_JSON) = apply {
            this.body = body
            this.contentType = contentType
        }

        fun addHeaders(vararg headers: Pair<String, String>) = addHeaders(headers.asList())
        fun addHeaders(headers: List<Pair<String, String>>) = apply { headers.forEach(::addHeader) }
        fun addHeader(header: Pair<String, String>) = addHeader(header.first, header.second)
        fun addHeader(name: String, value: String) = apply { headers[name] = value }

        fun addParameters(vararg parameters: Pair<String, String>) = addParameters(parameters.asList())
        fun addParameters(parameters: List<Pair<String, String>>) = apply { parameters.forEach(::addParameter) }
        fun addParameter(parameter: Pair<String, String>) = addParameter(parameter.first, parameter.second)
        fun addParameter(name: String, value: Any) = apply { parameters[name] = value.toString() }

        fun configure(requestConfigurer: NetworkRequestConfigurer) = apply { requestConfigurer.configure(this) }

        fun build(): NetworkRequest {
            check(body == null || (body != null && contentType != null)) { "Content type must be presented for body" }
            return NetworkRequest(
                method, requireNotNull(url), followRedirects, userAgent, body, contentType, headers, parameters
            )
        }

        fun interface NetworkRequestConfigurer {
            fun configure(builder: Builder)
        }
    }

    data class ContentType(
        val mimeType: String,
        val charset: Charset
    )

    companion object {
        val APPLICATION_FORM_URLENCODED = ContentType("application/x-www-form-urlencoded", ISO_8859_1)
        val MULTIPART_FORM_DATA = ContentType("multipart/form-data", ISO_8859_1)
        val APPLICATION_JSON = ContentType("application/json", UTF_8)
        val APPLICATION_XML = ContentType("application/xml", ISO_8859_1)
        val TEXT_HTML = ContentType("text/html", ISO_8859_1)
        val TEXT_PLAIN = ContentType("text/plain", ISO_8859_1)
    }
}