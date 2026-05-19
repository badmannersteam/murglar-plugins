package com.badmanners.murglar.lib.core.model.track.source

import com.badmanners.murglar.lib.core.service.Murglar
import com.badmanners.murglar.lib.core.utils.contract.Model
import java.io.Serializable
import java.util.Base64


/**
 * Source of track content.
 */
@Model
data class Source @JvmOverloads constructor(

    /**
     * Id of this source, unique per track (e.g. 'progressive_mp3_320')
     */
    val id: String,

    /**
     * Content url.
     *
     * The following schemas are supported:
     * - `http://...` or `https://...` link to a content/manifest file
     * - `data:<mime>;base64,<manifest content>` base64 encoded manifest content
     *
     * This field must be filled in the [Murglar.resolveSourceForUrl] if content url is unavailable
     * when [Source] object is created.
     */
    val url: String?,

    /**
     * Human-readable tag (e.g. 'MP3 320')
     */
    val tag: String,

    val extension: Extension,

    val container: Container,

    val bitrate: Bitrate,

    val qualityTier: QualityTier,

    /**
     * Size in bytes.
     */
    val size: Long = UNKNOWN_SIZE

) : Serializable {

    val isDataUrl: Boolean
        get() = url?.startsWith("data:") ?: false

    val data: SourceData
        get() {
            val url = checkNotNull(url)
            check(isDataUrl)

            val parts = url.removePrefix("data:").split(";base64,", limit = 2)
            check(parts.size == 2) { "Invalid data URL format: $url" }

            return SourceData(
                mime = parts[0],
                data = Base64.getDecoder().decode(parts[1])
            )
        }

    val hasSize: Boolean
        get() = size != UNKNOWN_SIZE

    fun copyWithNewUrl(url: String) = copy(url = url)

    override fun toString(): String = "$tag ($id $container $extension, $bitrate, $qualityTier, $size, $url)"

    data class SourceData(
        val mime: String,
        val data: ByteArray
    )

    companion object {
        private const val serialVersionUID = 1L
        const val UNKNOWN_SIZE: Long = -1
    }
}