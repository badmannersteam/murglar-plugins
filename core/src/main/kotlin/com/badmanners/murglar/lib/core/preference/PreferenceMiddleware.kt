package com.badmanners.murglar.lib.core.preference

import com.badmanners.murglar.lib.core.service.Murglar


/**
 * Preference middleware.
 *
 * Used in [Murglar]s for storing key-value data.
 */
interface PreferenceMiddleware {

    /**
     * Returns a set of all keys stored in preferences.
     *
     * @return Set of all stored preference keys
     */
    fun getKeys(): Set<String>

    /**
     * Checks if the given key exists in preferences.
     *
     * @param key Key to check
     * @return true if key exists, false otherwise
     */
    operator fun contains(key: String): Boolean

    /**
     * Retrieves a string value from preferences.
     *
     * @param key Key to retrieve
     * @param defValue Default value if key doesn't exist
     * @return Stored string value or default value
     */
    fun <S : String?> getString(key: String, defValue: S): S

    /**
     * Stores a string value in preferences.
     * Passing null value will remove the key.
     *
     * @param key Key to store value under
     * @param value String value to store
     */
    fun setString(key: String, value: String?)

    /**
     * Retrieves an integer value from preferences.
     *
     * @param key Key to retrieve
     * @param defValue Default value if key doesn't exist
     * @return Stored integer value or default value
     */
    fun <I : Int?> getInt(key: String, defValue: I): I

    /**
     * Stores an integer value in preferences.
     * Passing null value will remove the key.
     *
     * @param key Key to store value under
     * @param value Integer value to store
     */
    fun setInt(key: String, value: Int?)

    /**
     * Retrieves a long value from preferences.
     *
     * @param key Key to retrieve
     * @param defValue Default value if key doesn't exist
     * @return Stored long value or default value
     */
    fun <L : Long?> getLong(key: String, defValue: L): L

    /**
     * Stores a long value in preferences.
     * Passing null value will remove the key.
     *
     * @param key Key to store value under
     * @param value Long value to store
     */
    fun setLong(key: String, value: Long?)

    /**
     * Retrieves a float value from preferences.
     *
     * @param key Key to retrieve
     * @param defValue Default value if key doesn't exist
     * @return Stored float value or default value
     */
    fun <F : Float?> getFloat(key: String, defValue: F): F

    /**
     * Stores a float value in preferences.
     * Passing null value will remove the key.
     *
     * @param key Key to store value under
     * @param value Float value to store
     */
    fun setFloat(key: String, value: Float?)

    /**
     * Retrieves a boolean value from preferences.
     *
     * @param key Key to retrieve
     * @param defValue Default value if key doesn't exist
     * @return Stored boolean value or default value
     */
    fun <B : Boolean?> getBoolean(key: String, defValue: B): B

    /**
     * Stores a boolean value in preferences.
     * Passing null value will remove the key.
     *
     * @param key Key to store value under
     * @param value Boolean value to store
     */
    fun setBoolean(key: String, value: Boolean?)

    /**
     * Removes the specified key and its value from preferences.
     *
     * @param key Key to remove
     */
    fun remove(key: String)

    /**
     * Removes all keys and values from preferences.
     */
    fun clearAll()
}