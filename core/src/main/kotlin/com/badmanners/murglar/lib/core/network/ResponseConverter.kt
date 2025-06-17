package com.badmanners.murglar.lib.core.network

import com.badmanners.murglar.lib.core.utils.MurglarLibUtils
import kotlinx.serialization.ExperimentalSerializationApi
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
    suspend fun convert(stream: InputStream, charset: Charset): T
}


/**
 * Default [ResponseConverter]s.
 */
@OptIn(ExperimentalSerializationApi::class)
object ResponseConverters {

    /**
     * Converter for cases when you don't care about response body
     * or only headers are required (e.g. file size estimation with GET request and body dropping).
     */
    @JvmStatic
    fun asUnit() = object : ResponseConverter<Unit> {
        override val dropBody = true
        override val emptyResult = Unit
        override suspend fun convert(stream: InputStream, charset: Charset) = error("Can't convert empty body!")
    }

    @JvmStatic
    fun asByteArray() = ResponseConverter { stream, _ -> stream.readBytes() }

    @JvmStatic
    fun asString() = ResponseConverter { stream, charset -> String(stream.readBytes(), charset) }

    @JvmStatic
    fun asJsonObject() = ResponseConverter { stream, _ -> decodeFromStreamCompat<JsonObject>(stream) }

    @JvmStatic
    fun asJsonArray() = ResponseConverter { stream, _ -> decodeFromStreamCompat<JsonArray>(stream) }


    private inline fun <reified T> decodeFromStreamCompat(stream: InputStream) = when {
        // https://github.com/Kotlin/kotlinx.serialization/issues/2457
        MurglarLibUtils.isAndroid() && MurglarLibUtils.getAndroidSdkVersion()!! < 24 ->
            Json.decodeFromString<T>(stream.readBytes().toString(Charsets.UTF_8))

        else -> Json.decodeFromStream<T>(stream)
    }
}