package com.badmanners.murglar.lib.core.model.track.source


/**
 * Approximate [Source] bitrate.
 */
enum class Bitrate(val value: Int?) {

    /**
     * For any low bitrate.
     */
    B_32(32),

    B_64(64),

    B_96(96),

    B_128(128),

    B_160(160),

    B_192(192),

    B_256(256),

    B_320(320),

    /**
     * For FLAC/WAV/etc.
     */
    B_HI_RES(null),

    B_UNKNOWN(null);


    val text: String
        get() {
            return when (this) {
                B_HI_RES -> "Hi-Res"
                B_UNKNOWN -> "???"
                else -> value.toString()
            }
        }
}