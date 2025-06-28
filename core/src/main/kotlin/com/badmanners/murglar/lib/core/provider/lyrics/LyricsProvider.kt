package com.badmanners.murglar.lib.core.provider.lyrics

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.provider.ProviderException
import com.badmanners.murglar.lib.core.utils.RateLimit


/**
 * Lyrics provider.
 */
interface LyricsProvider {

    /**
     * @return unique id of this provider (e.g. 'GENIUS_LYRICS_PROVIDER').
     */
    val id: String

    /**
     * @return name of this provider (e.g. 'Genius').
     */
    val name: String

    /**
     * List of service rate limits.
     * May be empty if no rate limits are required.
     */
    val rateLimits: List<RateLimit>

    /**
     * Searches for the lyrics for provided track.
     *
     * @return [LyricsSearchResult] optional if any suitable result found,
     * empty optional if no lyrics found or found results don't match the query at all.
     * @throws ProviderException if lyrics search failed.
     */
    suspend fun searchLyrics(track: BaseTrack): LyricsSearchResult?
}