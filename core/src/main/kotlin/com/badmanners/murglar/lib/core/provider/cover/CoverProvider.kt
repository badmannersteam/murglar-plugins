package com.badmanners.murglar.lib.core.provider.cover

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.provider.ProviderException


/**
 * Cover provider.
 */
interface CoverProvider {

    /**
     * @return unique id of this provider (e.g. 'ITUNES_COVER_PROVIDER').
     */
    val id: String

    /**
     * @return name of this provider (e.g. 'iTunes').
     */
    val name: String

    /**
     * Searches for the cover for provided track.
     *
     * @return [CoverSearchResult] optional if any suitable result found,
     * empty optional if no cover found or found results don't match the query at all.
     * @throws ProviderException if cover search failed.
     */
    suspend fun searchCover(track: BaseTrack): CoverSearchResult?
}