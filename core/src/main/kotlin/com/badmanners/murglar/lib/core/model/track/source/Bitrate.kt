package com.badmanners.murglar.lib.core.model.track.source


/**
 * [Source] bitrate.
 *
 * Exact if known or [B_UNKNOWN] otherwise.
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

    B_LOSSLESS(null),

    B_HI_RES(null),

    B_UNKNOWN(null);


    val text: String
        get() = when (this) {
            B_LOSSLESS -> "Lossless"
            B_HI_RES -> "Hi-Res"
            B_UNKNOWN -> "???"
            else -> "$value kbps"
        }
}