package com.badmanners.murglar.lib.core.model.node

import com.badmanners.murglar.lib.core.model.radio.BaseRadio
import com.badmanners.murglar.lib.core.model.radio.RadioUpdate
import com.badmanners.murglar.lib.core.node.NodeResolver
import com.badmanners.murglar.lib.core.utils.contract.Model
import java.io.Serializable


/**
 * [RadioUpdate] typeless version for use in [NodeResolver].
 */
@Model
data class RadioNodeUpdate(
    val updatedRadio: BaseRadio,
    val nextTracks: List<Node>
) : Serializable