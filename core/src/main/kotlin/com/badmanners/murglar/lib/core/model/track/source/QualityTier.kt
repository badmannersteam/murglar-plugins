package com.badmanners.murglar.lib.core.model.track.source


/**
 * [Source] quality tier.
 *
 * Used for grouping similar combinations of [Bitrate] and [Extension].
 */
enum class QualityTier {

    /**
     * Low quality lossy compression (typically 64-96 kbps or lower).
     */
    LOW_QUALITY,

    /**
     * Normal quality lossy compression (typically 128-192 kbps).
     */
    NORMAL_QUALITY,

    /**
     * High quality lossy compression (typically 256-320 kbps).
     */
    HIGH_QUALITY,

    /**
     * Lossless quality (typically 16-bit/44.1kHz, FLAC/WAV).
     */
    LOSSLESS,

    /**
     * Hi-Res lossless quality (typically 24-bit/96kHz or higher).
     */
    HI_RES
}