package com.badmanners.murglar.lib.core.utils

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull


fun JsonObject.has(key: String): Boolean = containsKey(key)

val JsonElement.jsonObjectOpt: JsonObject? get() = if (this is JsonObject) jsonObject else null
val JsonElement.jsonArrayOpt: JsonArray? get() = if (this is JsonArray) jsonArray else null
val JsonElement.jsonPrimitiveOpt: JsonPrimitive? get() = if (this is JsonPrimitive) jsonPrimitive else null

val JsonElement.string: String get() = stringOpt ?: error(this)
val JsonElement.stringOpt: String? get() = jsonPrimitiveOpt?.contentOrNull

val JsonElement.int: Int get() = intOpt ?: error(this)
val JsonElement.intOpt: Int? get() = jsonPrimitiveOpt?.intOrNull

val JsonElement.boolean: Boolean get() = booleanOpt ?: error(this)
val JsonElement.booleanOpt: Boolean? get() = jsonPrimitiveOpt?.booleanOrNull

val JsonElement.long: Long get() = longOpt ?: error(this)
val JsonElement.longOpt: Long? get() = jsonPrimitiveOpt?.longOrNull

val JsonElement.double: Double get() = doubleOpt ?: error(this)
val JsonElement.doubleOpt: Double? get() = jsonPrimitiveOpt?.doubleOrNull

val JsonElement.float: Float get() = floatOpt ?: error(this)
val JsonElement.floatOpt: Float? get() = jsonPrimitiveOpt?.floatOrNull

fun JsonObject.getJsonObject(key: String): JsonObject = getJsonObjectOpt(key) ?: error(key)
fun JsonObject.getJsonObjectOpt(key: String): JsonObject? = this[key]?.jsonObjectOpt

fun JsonObject.getJsonArray(key: String): JsonArray = getJsonArrayOpt(key) ?: error(key)
fun JsonObject.getJsonArrayOpt(key: String): JsonArray? = this[key]?.jsonArrayOpt

fun JsonObject.getString(key: String): String = getStringOpt(key) ?: error(key)
fun JsonObject.getStringOpt(key: String): String? = this[key]?.stringOpt

fun JsonObject.getInt(key: String): Int = getIntOpt(key) ?: error(key)
fun JsonObject.getIntOpt(key: String): Int? = this[key]?.intOpt

fun JsonObject.getBoolean(key: String): Boolean = getBooleanOpt(key) ?: error(key)
fun JsonObject.getBooleanOpt(key: String): Boolean? = this[key]?.booleanOpt

fun JsonObject.getLong(key: String): Long = getLongOpt(key) ?: error(key)
fun JsonObject.getLongOpt(key: String): Long? = this[key]?.longOpt

fun JsonObject.getDouble(key: String): Double = getDoubleOpt(key) ?: error(key)
fun JsonObject.getDoubleOpt(key: String): Double? = this[key]?.doubleOpt

fun JsonObject.getFloat(key: String): Float = getFloatOpt(key) ?: error(key)
fun JsonObject.getFloatOpt(key: String): Float? = this[key]?.floatOpt

private fun error(element: JsonElement): Nothing = kotlin.error("Can't cast '$element'!")
private fun error(key: String): Nothing = kotlin.error("Can't get '$key'!")