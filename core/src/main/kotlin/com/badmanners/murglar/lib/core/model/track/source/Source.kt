package com.badmanners.murglar.lib.core.model.track.source

import com.badmanners.murglar.lib.core.service.Murglar
import com.badmanners.murglar.lib.core.utils.contract.Model
import java.io.Serializable


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

    val bitrate: Bitrate = Bitrate.B_UNKNOWN,

    /**
     * Size in bytes.
     */
    val size: Long = UNKNOWN_SIZE

) : Serializable {

    val hasSize: Boolean
        get() = size != UNKNOWN_SIZE

    fun copyWithNewUrl(url: String) = copy(url = url)

    override fun toString(): String = "$tag ($id $container $extension, $size, $url)"

    companion object {
        private const val serialVersionUID = 1L
        const val UNKNOWN_SIZE: Long = -1
    }
}