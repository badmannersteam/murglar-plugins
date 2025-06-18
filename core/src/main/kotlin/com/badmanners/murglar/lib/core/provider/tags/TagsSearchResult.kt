package com.badmanners.murglar.lib.core.provider.tags

import com.badmanners.murglar.lib.core.model.tag.Tags


/**
 * Tags search result.
 */
data class TagsSearchResult(

    /**
     * Tag of search result (e.g. full title of the track).
     * Can be used for results weights calculation.
     */
    val tag: String,

    /**
     * Tags.
     */
    val tags: Tags
)