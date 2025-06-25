package com.badmanners.murglar.lib.core.model.node

import com.badmanners.murglar.lib.core.utils.contract.Model


/**
 * Default implementation for generic directory nodes.
 */
@Model
class Directory(
    id: String,
    name: String,
    summary: String? = null,
    type: String = NodeType.NODE,
    smallCoverUrl: String? = null,
    bigCoverUrl: String? = null,
    serviceUrl: String? = null
) : BaseNode(id, name, summary, type, smallCoverUrl, bigCoverUrl, serviceUrl)