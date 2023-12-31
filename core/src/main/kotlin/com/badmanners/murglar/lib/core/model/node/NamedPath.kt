package com.badmanners.murglar.lib.core.model.node


/**
 * Node path with name.
 */
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
)