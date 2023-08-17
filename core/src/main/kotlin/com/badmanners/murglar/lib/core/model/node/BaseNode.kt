package com.badmanners.murglar.lib.core.model.node

import com.badmanners.murglar.lib.core.utils.contract.Model


/**
 * Base class for generic directory nodes.
 */
@Model
open class BaseNode(
    override val nodeId: String,
    override val nodeName: String,
    override val nodeSummary: String?,
    override val nodeType: String,
    override val smallCoverUrl: String? = null,
    override val bigCoverUrl: String? = null,
    override val serviceUrl: String? = null
) : MutableNode() {

    @JvmOverloads
    constructor(nodeId: String, name: String, type: String = NodeType.NODE) : this(nodeId, name, null, type)

    constructor(node: Node) : this(
        nodeId = node.nodeId,
        nodeName = node.nodeName,
        nodeSummary = node.nodeSummary,
        nodeType = node.nodeType,
        smallCoverUrl = node.smallCoverUrl,
        bigCoverUrl = node.bigCoverUrl
    ) {
        withNodeAttributes(node)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false

        other as BaseNode
        return nodeId == other.nodeId
    }

    override fun hashCode() = nodeId.hashCode()


    companion object {
        private const val serialVersionUID = 1L
    }
}