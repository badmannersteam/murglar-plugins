package com.badmanners.murglar.lib.core.provider.cover


/**
 * Cover search result.
 */
data class CoverSearchResult(

    /**
     * Tag of search result (e.g. full title of the track).
     * Can be used for results weights calculation.
     */
    val tag: String,

    /**
     * Resolved url of the small cover (up to 300px).
     */
    val smallCoverUrl: String,

    /**
     * Resolved url of the big cover (up to 1000-1500px).
     */
    val bigCoverUrl: String
)