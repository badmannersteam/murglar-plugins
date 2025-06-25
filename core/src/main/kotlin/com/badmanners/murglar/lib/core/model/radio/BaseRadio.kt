package com.badmanners.murglar.lib.core.model.radio

import com.badmanners.murglar.lib.core.model.node.MutableNode
import com.badmanners.murglar.lib.core.model.node.NodeType
import com.badmanners.murglar.lib.core.utils.contract.Model


/**
 * Base class for radio/station/wave/flow/etc (dynamic tracks list).
 */
@Model
abstract class BaseRadio(
    val id: String,
    val title: String,
    val description: String? = null,
    /**
     * Radio settings metadata, empty if settings are unsupported.
     */
    val settings: List<RadioSetting> = emptyList(),
    override val smallCoverUrl: String? = null,
    override val bigCoverUrl: String? = null,
    override val serviceUrl: String? = null
) : MutableNode() {
    override val nodeId: String by ::id
    override val nodeName: String by ::title
    override val nodeSummary: String? by ::description
    override val nodeType: String = NodeType.RADIO

    val hasSettings: Boolean
        get() = settings.isNotEmpty()
}