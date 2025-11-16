package com.badmanners.murglar.lib.core.provider.cover

import com.badmanners.murglar.lib.core.model.node.Node
import com.badmanners.murglar.lib.core.provider.ProviderException
import com.badmanners.murglar.lib.core.utils.RateLimit


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
     * List of service rate limits.
     * May be empty if no rate limits are required.
     */
    val rateLimits: List<RateLimit>

    /**
     * Searches the covers for the provided node.
     *
     * @return [CoverSearchResult] optional if any suitable result found,
     * empty optional if no covers found or found results don't match the query at all.
     * @throws ProviderException if cover search failed.
     */
    suspend fun searchCover(node: Node): CoverSearchResult?
}