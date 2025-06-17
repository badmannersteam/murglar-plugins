package com.badmanners.murglar.lib.core.model.track

import com.badmanners.murglar.lib.core.model.node.MutableNode
import com.badmanners.murglar.lib.core.model.node.NodeType
import com.badmanners.murglar.lib.core.model.track.source.Source
import com.badmanners.murglar.lib.core.utils.MediaId
import com.badmanners.murglar.lib.core.utils.contract.Model
import org.threeten.bp.LocalDate


/**
 * Base class for tracks.
 */
@Model
abstract class BaseTrack(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val artistIds: List<String>,
    val artistNames: List<String>,
    val albumId: String? = null,
    val albumName: String? = null,
    val albumReleaseDate: LocalDate? = null,
    val indexInAlbum: Int? = null,
    val volumeNumber: Int? = null,
    val durationMs: Long,
    val genre: String? = null,
    val explicit: Boolean = false,
    val gain: String? = null,
    val peak: String? = null,
    /**
     * List of all track's sources, descending by bitrate and priority.
     */
    val sources: List<Source>,
    /**
     * Media id that is unique for the whole world, for example "ynd_12345678".
     * @see MediaId
     */
    val mediaId: String,
    override val smallCoverUrl: String? = null,
    override val bigCoverUrl: String? = null,
    override val serviceUrl: String? = null
) : MutableNode() {

    override val nodeId: String = id
    override val nodeName: String by ::fullTitle
    override val nodeSummary: String by ::artists
    override val nodeType: String = NodeType.TRACK

    val fullTitle: String
        get() = when {
            !subtitle.isNullOrEmpty() -> "$title ($subtitle)"
            else -> title
        }

    val hasArtist: Boolean
        get() = artistIds.isNotEmpty() && artistNames.isNotEmpty()

    val artists: String
        get() = artistNames.joinToString(", ")

    val artistName: String?
        get() = artistNames.firstOrNull()

    val artistId: String?
        get() = artistIds.firstOrNull()

    val tag: String
        get() = "$artists - $fullTitle"

    val hasAlbum: Boolean
        get() = !albumId.isNullOrEmpty()

    val hasAlbumName: Boolean
        get() = !albumName.isNullOrEmpty()

    val hasAlbumReleaseDate: Boolean
        get() = albumReleaseDate != null

    val hasIndexInAlbum: Boolean
        get() = indexInAlbum != null

    val hasVolumeNumber: Boolean
        get() = volumeNumber != null

    val hasGenre: Boolean
        get() = !genre.isNullOrEmpty()

    val hasGain: Boolean
        get() = !gain.isNullOrEmpty()

    val hasPeak: Boolean
        get() = !peak.isNullOrEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false
        other as BaseTrack
        return mediaId == other.mediaId
    }

    override fun hashCode() = mediaId.hashCode()
}