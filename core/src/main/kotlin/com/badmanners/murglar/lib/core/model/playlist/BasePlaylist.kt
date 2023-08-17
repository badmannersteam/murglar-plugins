package com.badmanners.murglar.lib.core.model.playlist

import com.badmanners.murglar.lib.core.model.node.MutableNode
import com.badmanners.murglar.lib.core.model.node.NodeType
import com.badmanners.murglar.lib.core.utils.contract.Model


/**
 * Base class for playlists.
 */
@Model
abstract class BasePlaylist(
    val id: String,
    val title: String,
    val description: String? = null,
    val tracksCount: Int,
    val explicit: Boolean = false,
    override val smallCoverUrl: String? = null,
    override val bigCoverUrl: String? = null,
    override val serviceUrl: String? = null
) : MutableNode() {

    override val nodeId: String by ::id
    override val nodeName: String by ::title
    override val nodeSummary: String by ::fullDescription
    override val nodeType: String = NodeType.PLAYLIST

    val hasDescription: Boolean
        get() = !description.isNullOrEmpty()

    open val fullDescription: String
        get() = buildString {
            if (tracksCount > 0)
                append(tracksCount).append(" ♫")
            if (!description.isNullOrEmpty()) {
                if (!isEmpty())
                    append(" - ")
                append(description)
            }
        }
}