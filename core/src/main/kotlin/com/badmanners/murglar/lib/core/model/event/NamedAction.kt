package com.badmanners.murglar.lib.core.model.event

import com.badmanners.murglar.lib.core.node.NodeResolver
import com.badmanners.murglar.lib.core.utils.contract.Model
import java.io.Serializable


/**
 * Custom named action.
 *
 * @see NodeResolver.handleEvent
 */
@Model
data class NamedAction(

    /**
     * Name for action.
     */
    val name: String,

    /**
     * Action.
     */
    val action: CustomAction
) : Serializable
