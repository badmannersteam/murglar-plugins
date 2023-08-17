package com.badmanners.murglar.lib.core.preference

import com.badmanners.murglar.lib.core.service.Murglar


/**
 * Preference middleware.
 *
 * Used in [Murglar]s for storing key-value data.
 */
interface PreferenceMiddleware {

    fun getKeys(): Set<String>

    operator fun contains(key: String): Boolean

    fun getString(key: String, defValue: String): String
    fun setString(key: String, value: String)

    fun getInt(key: String, defValue: Int): Int
    fun setInt(key: String, value: Int)

    fun getLong(key: String, defValue: Long): Long
    fun setLong(key: String, value: Long)

    fun getFloat(key: String, defValue: Float): Float
    fun setFloat(key: String, value: Float)

    fun getBoolean(key: String, defValue: Boolean): Boolean
    fun setBoolean(key: String, value: Boolean)

    fun remove(key: String)

    fun clearAll()
}