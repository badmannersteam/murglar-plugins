package com.badmanners.murglar.lib.core.provider.lyrics

import com.badmanners.murglar.lib.core.model.tag.Lyrics


/**
 * Lyrics search result.
 */
data class LyricsSearchResult(

    /**
     * Tag of search result (e.g. full title of the track).
     * Can be used for results weights calculation.
     */
    val tag: String,

    /**
     * Lyrics.
     */
    val lyrics: Lyrics
)