package com.badmanners.murglar.lib.core.model.event

import com.badmanners.murglar.lib.core.node.NodeResolver


/**
 * Custom named action.
 *
 * @see NodeResolver.handleEvent
 */
data class NamedAction(

    /**
     * Name for action.
     */
    val name: String,

    /**
     * Action.
     */
    val action: CustomAction
)
