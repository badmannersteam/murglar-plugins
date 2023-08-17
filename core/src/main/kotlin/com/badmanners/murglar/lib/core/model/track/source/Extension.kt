package com.badmanners.murglar.lib.core.model.track.source


/**
 * [Source] extension of extracted from [Container] content.
 */
enum class Extension {

    MP3,

    FLAC,

    WAV,

    AAC,

    OPUS,

    OGG,

    MP4,

    M4A,

    WEBM,

    WMA,

    /**
     * For this case extension can be extracted from response headers when downloading.
     */
    UNKNOWN;


    val value: String
        get() = name.lowercase()
}