package com.badmanners.murglar.lib.core.utils

import com.badmanners.murglar.lib.core.utils.PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy
import java.util.concurrent.TimeUnit


/**
 * Decorates a `Map` to evict expired entries once their expiration
 * time has been reached.
 *
 *
 * When putting a key-value pair in the map this decorator uses a
 * [ConstantTimeToLiveExpirationPolicy] to determine how long the entry should remain alive
 * as defined by an expiration time value.
 *
 *
 *
 * When accessing the mapped value for a key, its expiration time is checked,
 * and if it is a negative value or if it is greater than the current time, the
 * mapped value is returned. Otherwise, the key is removed from the decorated
 * map, and `null` is returned.
 *
 *
 *
 * When invoking methods that involve accessing the entire map contents (i.e
 * [.containsValue], [.entrySet], etc.) this decorator
 * removes all expired entries prior to actually completing the invocation.
 *
 *
 *
 * **Note that [PassiveExpiringMap] is not synchronized and is not
 * thread-safe.** If you wish to use this map from multiple threads
 * concurrently, you must use appropriate synchronization. The simplest approach
 * is to wrap this map using [java.util.Collections.synchronizedMap].
 * This class may throw exceptions when accessed by concurrent threads without
 * synchronization.
 *
 *
 * @param <K> the type of the keys in this map
 * @param <V> the type of the values in this map
 * @since 4.0
 */
class PassiveExpiringMap<K, V> private constructor(
    expiringPolicy: ConstantTimeToLiveExpirationPolicy<K, V>,
    map: Map<K, V>
) : HashMap<K, V>(map) {

    /**
     * An ExpirationPolicy
     * that returns an expiration time that is a
     * constant about of time in the future from the current time.
     *
     * @param <K> the type of the keys in the map
     * @param <V> the type of the values in the map
     * @since 4.0
     */
    private class ConstantTimeToLiveExpirationPolicy<K, V> {

        /**
         * the constant time-to-live value measured in milliseconds.
         */
        private val timeToLiveMillis: Long

        /**
         * Construct a policy with the given time-to-live constant measured in
         * milliseconds. A negative time-to-live value indicates entries never
         * expire. A zero time-to-live value indicates entries expire (nearly)
         * immediately.
         *
         * @param timeToLiveMillis the constant amount of time (in milliseconds)
         * an entry is available before it expires. A negative value
         * results in entries that NEVER expire. A zero value results in
         * entries that ALWAYS expire.
         */
        constructor(timeToLiveMillis: Long) {
            this.timeToLiveMillis = timeToLiveMillis
        }

        /**
         * Determine the expiration time for the given key-value entry.
         *
         * @param key   the key for the entry (ignored).
         * @param value the value for the entry (ignored).
         * @return if [.timeToLiveMillis]  0, an expiration time of
         * [.timeToLiveMillis] +
         * [System.currentTimeMillis] is returned. Otherwise, -1
         * is returned indicating the entry never expires.
         */
        fun expirationTime(key: K, value: V): Long {
            if (timeToLiveMillis >= 0L) {
                // avoid numerical overflow
                val nowMillis = System.currentTimeMillis()
                return if (nowMillis > Long.MAX_VALUE - timeToLiveMillis) {
                    // expiration would be greater than Long.MAX_VALUE
                    // never expire
                    -1
                } else nowMillis + timeToLiveMillis

                // timeToLiveMillis in the future
            }

            // never expire
            return -1L
        }
    }

    /**
     * map used to manage expiration times for the actual map entries.
     */
    private val expirationMap: MutableMap<K, Long> = HashMap()

    /**
     * the policy used to determine time-to-live values for map entries.
     */
    private val expiringPolicy: ConstantTimeToLiveExpirationPolicy<K, V>

    /**
     * Construct a map decorator that decorates the given map using the given
     * time-to-live value measured in milliseconds to create and use a
     * [ConstantTimeToLiveExpirationPolicy] expiration policy.
     *
     * @param timeToLiveMillis the constant amount of time (in milliseconds) an
     * entry is available before it expires. A negative value results in
     * entries that NEVER expire. A zero value results in entries that
     * ALWAYS expire.
     */
    /**
     * Default constructor. Constructs a map decorator that results in entries
     * NEVER expiring.
     */
    @JvmOverloads
    constructor(timeToLiveMillis: Long = -1L)
            : this(ConstantTimeToLiveExpirationPolicy<K, V>(timeToLiveMillis), HashMap<K, V>())

    /**
     * Construct a map decorator using the given time-to-live value measured in
     * milliseconds to create and use a
     * [ConstantTimeToLiveExpirationPolicy] expiration policy. If there
     * are any elements already in the map being decorated, they will NEVER
     * expire unless they are replaced.
     *
     * @param timeToLiveMillis the constant amount of time (in milliseconds) an
     * entry is available before it expires. A negative value results in
     * entries that NEVER expire. A zero value results in entries that
     * ALWAYS expire.
     * @param map              the map to decorate, must not be null.
     * @throws NullPointerException if the map is null.
     */
    constructor(timeToLiveMillis: Long, map: Map<K, V>)
            : this(ConstantTimeToLiveExpirationPolicy<K, V>(timeToLiveMillis), map)

    /**
     * Construct a map decorator using the given time-to-live value measured in
     * the given time units of measure to create and use a
     * [ConstantTimeToLiveExpirationPolicy] expiration policy.
     *
     * @param timeToLive the constant amount of time an entry is available
     * before it expires. A negative value results in entries that NEVER
     * expire. A zero value results in entries that ALWAYS expire.
     * @param timeUnit   the unit of time for the `timeToLive`
     * parameter, must not be null.
     * @throws NullPointerException if the time unit is null.
     */
    constructor(timeToLive: Long, timeUnit: TimeUnit) : this(validateAndConvertToMillis(timeToLive, timeUnit))

    /**
     * Construct a map decorator that decorates the given map using the given
     * time-to-live value measured in the given time units of measure to create
     * [ConstantTimeToLiveExpirationPolicy] expiration policy. This policy
     * is used to determine expiration times. If there are any elements already
     * in the map being decorated, they will NEVER expire unless they are
     * replaced.
     *
     * @param timeToLive the constant amount of time an entry is available
     * before it expires. A negative value results in entries that NEVER
     * expire. A zero value results in entries that ALWAYS expire.
     * @param timeUnit   the unit of time for the `timeToLive`
     * parameter, must not be null.
     * @param map        the map to decorate, must not be null.
     * @throws NullPointerException if the map or time unit is null.
     */
    constructor(timeToLive: Long, timeUnit: TimeUnit, map: Map<K, V>)
            : this(validateAndConvertToMillis(timeToLive, timeUnit), map)

    /**
     * Constructs a map decorator that decorates the given map and results in
     * entries NEVER expiring. If there are any elements already in the map
     * being decorated, they also will NEVER expire.
     *
     * @param map the map to decorate, must not be null.
     * @throws NullPointerException if the map is null.
     */
    constructor(map: Map<K, V>) : this(-1L, map)

    /**
     * Construct a map decorator that decorates the given map and uses the given
     * expiration policy to determine expiration times. If there are any
     * elements already in the map being decorated, they will NEVER expire
     * unless they are replaced.
     *
     * @param expiringPolicy the policy used to determine expiration times of
     * entries as they are added.
     * @param map            the map to decorate, must not be null.
     * @throws NullPointerException if the map or expiringPolicy is null.
     */
    init {
        this.expiringPolicy = expiringPolicy
    }

    /**
     * Normal [Map.clear] behavior with the addition of clearing all
     * expiration entries as well.
     */
    override fun clear() {
        super.clear()
        expirationMap.clear()
    }

    /**
     * All expired entries are removed from the map prior to determining the
     * contains result.
     * {@inheritDoc}
     */
    override fun containsKey(key: K): Boolean {
        removeIfExpired(key, now())
        return super.containsKey(key)
    }

    /**
     * All expired entries are removed from the map prior to determining the
     * contains result.
     * {@inheritDoc}
     */
    override fun containsValue(value: V): Boolean {
        removeAllExpired(now())
        return super.containsValue(value)
    }

    /**
     * All expired entries are removed from the map prior to returning the entry set.
     * {@inheritDoc}
     */
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() {
            removeAllExpired(now())
            return super.entries
        }

    /**
     * All expired entries are removed from the map prior to returning the entry value.
     * {@inheritDoc}
     */
    override operator fun get(key: K): V? {
        removeIfExpired(key, now())
        return super.get(key)
    }

    /**
     * All expired entries are removed from the map prior to determining if it is empty.
     * {@inheritDoc}
     */
    override fun isEmpty(): Boolean {
        removeAllExpired(now())
        return super.isEmpty()
    }

    /**
     * Determines if the given expiration time is less than `now`.
     *
     * @param now the time in milliseconds used to compare against the expiration time.
     * @param expirationTimeObject the expiration time value retrieved from [.expirationMap], can be null.
     * @return `true` if `expirationTimeObject` is  0  and `expirationTimeObject` &lt; `now`, `false` otherwise.
     */
    private fun isExpired(now: Long, expirationTimeObject: Long): Boolean {
        return expirationTimeObject in 0..now
    }

    /**
     * All expired entries are removed from the map prior to returning the key set.
     * {@inheritDoc}
     */
    override val keys: MutableSet<K>
        get() {
            removeAllExpired(now())
            return super.keys
        }

    /**
     * The current time in milliseconds.
     */
    private fun now(): Long {
        return System.currentTimeMillis()
    }

    /**
     * Add the given key-value pair to this map as well as recording the entry's expiration time based on
     * the current time in milliseconds and this map's [.expiringPolicy].
     *
     *
     * {@inheritDoc}
     */
    override fun put(key: K, value: V): V? {
        // remove the previous record
        removeIfExpired(key, now())

        // record expiration time of new entry
        val expirationTime = expiringPolicy.expirationTime(key, value)
        expirationMap[key] = expirationTime
        return super.put(key, value)
    }

    override fun putAll(from: Map<out K, V>) {
        for ((key, value) in from) {
            put(key, value)
        }
    }

    /**
     * Normal [Map.remove] behavior with the addition of removing
     * any expiration entry as well.
     * {@inheritDoc}
     */
    override fun remove(key: K): V? {
        expirationMap.remove(key)
        return super.remove(key)
    }

    /**
     * Removes all entries in the map whose expiration time is less than
     * `now`. The exceptions are entries with negative expiration
     * times; those entries are never removed.
     *
     * @see .isExpired
     */
    private fun removeAllExpired(nowMillis: Long) {
        val iter = expirationMap.entries.iterator()
        while (iter.hasNext()) {
            val (key, value) = iter.next()
            if (isExpired(nowMillis, value)) {
                // remove entry from collection
                super.remove(key)
                // remove entry from expiration map
                iter.remove()
            }
        }
    }

    /**
     * Removes the entry with the given key if the entry's expiration time is
     * less than `now`. If the entry has a negative expiration time,
     * the entry is never removed.
     */
    private fun removeIfExpired(key: K, nowMillis: Long) {
        val expirationTimeObject = expirationMap[key]
        if (isExpired(nowMillis, expirationTimeObject!!)) {
            remove(key)
        }
    }

    /**
     * All expired entries are removed from the map prior to returning the size.
     * {@inheritDoc}
     */
    override val size: Int
        get() {
            removeAllExpired(now())
            return super.size
        }

    /**
     * All expired entries are removed from the map prior to returning the value collection.
     * {@inheritDoc}
     */
    override val values: MutableCollection<V>
        get() {
            removeAllExpired(now())
            return super.values
        }

    companion object {
        /**
         * First validate the input parameters. If the parameters are valid, convert
         * the given time measured in the given units to the same time measured in
         * milliseconds.
         *
         * @param timeToLive the constant amount of time an entry is available
         * before it expires. A negative value results in entries that NEVER
         * expire. A zero value results in entries that ALWAYS expire.
         * @param timeUnit   the unit of time for the `timeToLive`
         * parameter, must not be null.
         * @throws NullPointerException if the time unit is null.
         */
        private fun validateAndConvertToMillis(timeToLive: Long, timeUnit: TimeUnit): Long =
            TimeUnit.MILLISECONDS.convert(timeToLive, timeUnit)
    }
}