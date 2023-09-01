package com.badmanners.murglar.lib.core.provider.lyrics

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.provider.ProviderException
import com.badmanners.murglar.lib.core.utils.contract.WorkerThread


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
     * Searches for the lyrics for provided track.
     *
     * @return [LyricsSearchResult] optional if any suitable result found,
     * empty optional if no lyrics found or found results don't match the query at all.
     * @throws ProviderException if lyrics search failed.
     */
    @WorkerThread
    fun searchLyrics(track: BaseTrack): LyricsSearchResult?
}