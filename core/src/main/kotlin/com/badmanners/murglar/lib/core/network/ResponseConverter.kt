package com.badmanners.murglar.lib.core.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream
import java.nio.charset.Charset


/**
 * Response body content converter from bytes stream.
 */
fun interface ResponseConverter<T> {

    /**
     * Client must drop loading body of response, if true.
     * In this case [emptyResult] will be returned instead of result of [convert].
     */
    val dropBody: Boolean
        get() = false

    /**
     * Result when body is dropped.
     */
    val emptyResult: T
        get() = error("No body!")

    /**
     * Performs response body content conversion.
     */
    fun convert(stream: InputStream, charset: Charset): T
}


/**
 * Default [ResponseConverter]s.
 */
object ResponseConverters {

    /**
     * Converter for cases when you don't care about response body
     * or only headers are required (e.g. file size estimation with GET request and body dropping).
     */
    @JvmStatic
    fun asUnit() = object : ResponseConverter<Unit> {
        override val dropBody = true
        override val emptyResult = Unit
        override fun convert(stream: InputStream, charset: Charset) = error("Can't convert empty body!")
    }

    @JvmStatic
    fun asByteArray() = ResponseConverter { stream, _ -> stream.readBytes() }

    @JvmStatic
    fun asString() = ResponseConverter { stream, charset -> String(stream.readBytes(), charset) }

    @JvmStatic
    fun asJsonObject() = ResponseConverter { stream, _ -> Json.decodeFromStream<JsonObject>(stream) }

    @JvmStatic
    fun asJsonArray() = ResponseConverter { stream, _ -> Json.decodeFromStream<JsonArray>(stream) }
}