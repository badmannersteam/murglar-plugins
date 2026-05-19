package com.badmanners.murglar.lib.core.model.track.source

import com.badmanners.murglar.lib.core.service.Murglar


/**
 * [Source] format for [Murglar].
 *
 * Describes the technical characteristics and availability of a specific audio format.
 */
data class SourceFormat(

    /**
     * File extension of the format.
     */
    val extension: Extension,

    /**
     * Bitrate of the format.
     */
    val bitrate: Bitrate,

    /**
     * Quality tier classification of the format.
     */
    val qualityTier: QualityTier,

    /**
     * Availability status of this format.
     */
    val availability: SourceFormatAvailability
) {

    /**
     * Availability status of a format.
     */
    enum class SourceFormatAvailability {
        /**
         * Format is available for use.
         */
        AVAILABLE,

        /**
         * Format requires an active service subscription.
         */
        REQUIRES_SERVICE_SUBSCRIPTION
    }
}
