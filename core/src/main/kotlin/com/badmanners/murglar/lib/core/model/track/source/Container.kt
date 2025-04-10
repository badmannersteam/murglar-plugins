package com.badmanners.murglar.lib.core.model.track.source


/**
 * [Source] container.
 */
enum class Container {

    /**
     * Pure progressive stream, like generic MP3/FLAC/AAC/WAV/etc file.
     */
    PROGRESSIVE,

    /**
     * MPEG-4 Part 14 container file.
     *
     * Corresponding [Source.extension] must be set to the actual audio track format, contained in the file,
     * because client may demux the audio track from the container.
     */
    MP4,

    /**
     * HLS stream.
     *
     * Corresponding [Source.extension] must be set to the actual audio track format, contained in the file,
     * because client may demux the audio track from the container.
     */
    HLS,

    /**
     * DASH stream.
     *
     * Corresponding [Source.extension] must be set to the actual audio track format, contained in the file,
     * because client may demux the audio track from the container.
     */
    DASH
}