package com.badmanners.murglar.lib.core.provider.tags

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.provider.ProviderException
import com.badmanners.murglar.lib.core.utils.RateLimit


/**
 * Tags provider.
 */
interface TagsProvider {

    /**
     * @return unique id of this provider (e.g. 'ITUNES_TAG_PROVIDER').
     */
    val id: String

    /**
     * @return name of this provider (e.g. 'iTunes').
     */
    val name: String

    /**
     * List of service rate limits.
     * May be empty if no rate limits are required.
     */
    val rateLimits: List<RateLimit>

    /**
     * Searches for the tags for provided track.
     *
     * @return [TagsSearchResult] optional if any suitable result found,
     * empty optional if no tags found or found results don't match the query at all.
     * @throws ProviderException if tags search failed.
     */
    suspend fun searchTags(track: BaseTrack): TagsSearchResult?
}