package com.badmanners.murglar.lib.core.model.node

import com.badmanners.murglar.lib.core.node.NodeResolver
import com.badmanners.murglar.lib.core.utils.contract.Model


/**
 * Base [Node] impl with mutable fields for node attributes.
 *
 * Designed for internal use in [NodeResolver]s, because node attributes are not known at the object construction time.
 *
 * Clients must use [Node].
 */
@Model
abstract class MutableNode : Node {

    final override lateinit var nodeParameters: NodeParameters
    final override lateinit var nodePath: Path

    fun withNodeParameters(nodeParameters: NodeParameters) = apply { this.nodeParameters = nodeParameters }

    fun withNodePath(path: Path) = apply { this.nodePath = path }

    fun withNodeAttributes(from: Node) = withNodeParameters(from.nodeParameters).withNodePath(from.nodePath)

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false
        other as MutableNode
        return nodePath == other.nodePath
    }

    override fun hashCode() = nodePath.hashCode()
}