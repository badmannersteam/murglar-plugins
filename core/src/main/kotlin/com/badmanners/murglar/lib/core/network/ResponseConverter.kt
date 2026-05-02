package com.badmanners.murglar.lib.core.network

import com.badmanners.murglar.lib.core.utils.MurglarLibUtils.getAndroidSdkVersion
import com.badmanners.murglar.lib.core.utils.MurglarLibUtils.isAndroid
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
interface ResponseConverter<T> {

    /**
     * Client must drop loading body of response, if true.
     * In this case [emptyResult] will be returned instead of result of [convert].
     */
    val dropBody: Boolean

    /**
     * Result when body is dropped.
     */
    val emptyResult: T

    /**
     * Performs response body content conversion.
     */
    suspend fun convert(stream: InputStream, charset: Charset): T
}

abstract class BaseResponseConverter<T> : ResponseConverter<T> {
    override val dropBody = false
    override val emptyResult: T get() = error("No body!")
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
    fun asByteArray() = object : BaseResponseConverter<ByteArray>() {
        override suspend fun convert(stream: InputStream, charset: Charset) = stream.readBytes()
    }

    @JvmStatic
    fun asString() = object : BaseResponseConverter<String>() {
        override suspend fun convert(stream: InputStream, charset: Charset) = String(stream.readBytes(), charset)
    }

    @JvmStatic
    fun asJsonObject() = Json.asModel<JsonObject>()

    @JvmStatic
    fun Json.asJsonObject() = asModel<JsonObject>()

    @JvmStatic
    fun asJsonArray() = Json.asModel<JsonArray>()

    @JvmStatic
    fun Json.asJsonArray() = asModel<JsonArray>()

    @JvmStatic
    inline fun <reified T> asModel() = Json.asModel<T>()

    @JvmStatic
    inline fun <reified T> Json.asModel() = object : BaseResponseConverter<T>() {
        override suspend fun convert(stream: InputStream, charset: Charset) = when {
            // https://github.com/Kotlin/kotlinx.serialization/issues/2457
            isAndroid() && getAndroidSdkVersion()!! < 24 ->
                decodeFromString<T>(stream.readBytes().toString(Charsets.UTF_8))

            else -> decodeFromStream<T>(stream)
        }
    }
}