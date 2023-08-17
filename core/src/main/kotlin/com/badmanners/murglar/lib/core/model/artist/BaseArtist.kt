package com.badmanners.murglar.lib.core.model.artist

import com.badmanners.murglar.lib.core.model.node.MutableNode
import com.badmanners.murglar.lib.core.model.node.NodeType
import com.badmanners.murglar.lib.core.utils.contract.Model


/**
 * Base artist class.
 */
@Model
abstract class BaseArtist(
    val id: String,
    val name: String,
    override val smallCoverUrl: String? = null,
    override val bigCoverUrl: String? = null,
    override val serviceUrl: String? = null
) : MutableNode() {
    override val nodeId: String = id
    override val nodeName: String = name
    override val nodeSummary: String? = null
    override val nodeType: String = NodeType.ARTIST
}