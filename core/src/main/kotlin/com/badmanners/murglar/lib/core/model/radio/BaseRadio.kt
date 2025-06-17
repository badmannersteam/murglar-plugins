package com.badmanners.murglar.lib.core.model.radio

import com.badmanners.murglar.lib.core.model.node.BaseNode
import com.badmanners.murglar.lib.core.model.node.NodeType
import com.badmanners.murglar.lib.core.utils.contract.Model


/**
 * Base class for radio/station/wave/flow/etc (dynamic tracks list).
 */
@Model
open class BaseRadio(
    id: String,
    name: String,
    summary: String? = null,
    smallCoverUrl: String? = null,
    bigCoverUrl: String? = null
) : BaseNode(id, name, summary, NodeType.RADIO, smallCoverUrl, bigCoverUrl)