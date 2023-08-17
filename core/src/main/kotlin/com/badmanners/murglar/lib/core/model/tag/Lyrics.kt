package com.badmanners.murglar.lib.core.model.tag

import org.apache.commons.lang3.time.DurationFormatUtils.formatDuration


/**
 * Lyrics content.
 */
data class Lyrics @JvmOverloads constructor(

    /**
     * Plain unsynced lyrics.
     */
    val plain: String,

    /**
     * Optional synced lyrics.
     */
    val synced: SyncedLyrics? = null
) {

    val hasSynced
        get() = synced != null


    /**
     * Synced lyrics.
     */
    data class SyncedLyrics @JvmOverloads constructor(
        val lines: List<Line>,
        val artist: String,
        val title: String,
        val album: String? = null
    ) {

        fun buildAsLrcWithHeader() = buildString {
            append("[ar:").append(artist).append("]\n")
            if (!album.isNullOrEmpty())
                append("[al:").append(album).append("]\n")
            append("[ti:").append(title).append("]\n\n")
            append(buildAsLrc())
        }

        fun buildAsLrc() = lines.joinToString("\n") {
            "[${it.startMillis.asTimestamp()}]${it.text.orEmpty()}"
        }

        override fun toString() = buildAsLrc()

        private fun Long.asTimestamp() = formatDuration(this, "mm:ss.SS").dropLast(1)

        /**
         * Single line of synced lyrics.
         */
        data class Line @JvmOverloads constructor(

            /**
             * Line start time in milliseconds relative to track start.
             */
            val startMillis: Long,

            /**
             * Line duration in milliseconds.
             */
            val durationMillis: Long? = null,

            /**
             * Line text.
             */
            val text: String? = null
        )
    }
}