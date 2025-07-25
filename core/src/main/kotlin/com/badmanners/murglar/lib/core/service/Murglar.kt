package com.badmanners.murglar.lib.core.service

import Versions
import com.badmanners.murglar.lib.core.decrypt.Decryptor
import com.badmanners.murglar.lib.core.localization.Messages
import com.badmanners.murglar.lib.core.login.LoginResolver
import com.badmanners.murglar.lib.core.model.node.Node
import com.badmanners.murglar.lib.core.model.tag.Lyrics
import com.badmanners.murglar.lib.core.model.tag.Tags
import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.model.track.source.Bitrate
import com.badmanners.murglar.lib.core.model.track.source.Extension
import com.badmanners.murglar.lib.core.model.track.source.Source
import com.badmanners.murglar.lib.core.node.NodeResolver
import com.badmanners.murglar.lib.core.preference.Preference
import com.badmanners.murglar.lib.core.utils.RateLimit
import java.util.Locale


/**
 * Main service class.
 *
 * Used as entry point for interaction with music service.
 *
 * As a base for implementations use [BaseMurglar].
 */
interface Murglar<Track : BaseTrack> {

    companion object {
        /**
         * Version of library API, can be used in plugins as compatibility check.
         */
        const val MURGLAR_LIB_VERSION = Versions.murglarPluginsMajor
    }

    /**
     * Unique id of this [Murglar] (e.g. 'dzr', 'ynd', 'sc').
     */
    val id: String

    /**
     * Human-readable name of the music service (e.g. 'Deezer', 'Yandex Music', 'SoundCloud').
     */
    val name: String

    /**
     * List of all available locales for [Messages].
     * Must contain at least [Locale.ENGLISH] locale.
     */
    val availableLocales: List<Locale>

    /**
     * Current selected locale.
     * May be updated with value from [availableLocales].
     */
    var locale: Locale

    /**
     *  List of all [Preference]s, that must be provided to user.
     */
    val murglarPreferences: List<Preference>

    /**
     *  Descending list of all possible formats, that [Source]s from this [Murglar] can contain.
     */
    val possibleFormats: List<Pair<Extension, Bitrate>>

    /**
     * List of service rate limits.
     * May be empty if no rate limits are required.
     */
    val rateLimits: List<RateLimit>

    /**
     * [LoginResolver] for this [Murglar].
     */
    val loginResolver: LoginResolver

    /**
     *  [NodeResolver] for this [Murglar].
     */
    val nodeResolver: NodeResolver

    /**
     * [Decryptor] for this [Murglar].
     */
    val decryptor: Decryptor<Track>

    /**
     * Lifecycle method that will be called by client on client startup.
     */
    suspend fun onCreate()

    /**
     * Resolves passed [Source] of the [BaseTrack] for final audio content url.
     */
    suspend fun resolveSourceForUrl(track: Track, source: Source): Source

    /**
     * Returns true if the passed track has lyrics (or at least can have), false otherwise.
     */
    fun hasLyrics(track: Track): Boolean

    /**
     * Returns track's [Lyrics].
     * [Murglar.hasLyrics] must be checked before.
     */
    suspend fun getLyrics(track: Track): Lyrics

    /**
     * Return track's [Tags].
     *
     * @param parent optional context parent, like an album or playlist.
     */
    suspend fun getTags(track: Track, parent: Node?): Tags

    /**
     * Returns all possible tracks by media ids.
     */
    suspend fun getTracksByMediaIds(mediaIds: List<String>): List<Track>
}