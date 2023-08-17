package com.badmanners.murglar.lib.core.model.node


/**
 * Batch of node and its content.
 */
data class NodeWithContent(
    val node: Node,
    val content: List<Node>
)