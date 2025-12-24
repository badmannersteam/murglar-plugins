package com.badmanners.murglar.lib.core.model.node

import com.badmanners.murglar.lib.core.utils.contract.Model
import java.io.Serializable


/**
 * Node path with name.
 */
@Model
data class NamedPath(

    /**
     * Name for node path (artist/album/etc name).
     */
    val name: String,

    /**
     * [NodeType] of the node.
     */
    val type: String,

    /**
     * Node path.
     */
    val path: Path
) : Serializable