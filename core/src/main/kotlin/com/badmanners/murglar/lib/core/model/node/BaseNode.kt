package com.badmanners.murglar.lib.core.model.node

import com.badmanners.murglar.lib.core.utils.contract.Model


/**
 * Base class for generic directory nodes.
 */
@Model
abstract class BaseNode(
    override val nodeId: String,
    override val nodeName: String,
    override val nodeSummary: String? = null,
    override val nodeType: String = NodeType.NODE,
    override val smallCoverUrl: String? = null,
    override val bigCoverUrl: String? = null,
    override val serviceUrl: String? = null
) : MutableNode()