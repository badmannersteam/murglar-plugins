package com.badmanners.murglar.lib.core.model.album

import com.badmanners.murglar.lib.core.model.node.NodeType
import com.badmanners.murglar.lib.core.model.playlist.BasePlaylist
import com.badmanners.murglar.lib.core.utils.contract.Model
import org.threeten.bp.LocalDate


/**
 * Base class for albums.
 */
@Model
abstract class BaseAlbum(
    id: String,
    title: String,
    description: String? = null,
    val type: AlbumType = AlbumType.ALBUM,
    val artistIds: List<String>,
    val artistNames: List<String>,
    tracksCount: Int,
    val releaseDate: LocalDate? = null,
    val genre: String? = null,
    explicit: Boolean = false,
    smallCoverUrl: String? = null,
    bigCoverUrl: String? = null,
    serviceUrl: String? = null
) : BasePlaylist(id, title, description, tracksCount, explicit, smallCoverUrl, bigCoverUrl, serviceUrl) {

    override val nodeType: String = NodeType.ALBUM

    override val fullDescription: String
        get() = buildString {
            releaseDate?.let {
                append(it.year).append(" - ")
            }
            description.takeIf { !it.isNullOrEmpty() }?.let {
                append(description).append(" - ")
            }
            append(artistNames.joinToString(", "))
            if (tracksCount > 0) {
                if (!isEmpty())
                    append(" - ")
                append(tracksCount).append(" ♫")
            }
        }

    val hasArtist: Boolean
        get() = artistIds.isNotEmpty() && artistNames.isNotEmpty()

    val artistName: String?
        get() = artistNames.firstOrNull()

    val artistId: String?
        get() = artistIds.firstOrNull()
}