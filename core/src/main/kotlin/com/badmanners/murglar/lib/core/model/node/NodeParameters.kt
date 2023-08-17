package com.badmanners.murglar.lib.core.model.node

import com.badmanners.murglar.lib.core.model.node.NodeParameters.PagingType.NON_PAGEABLE
import com.badmanners.murglar.lib.core.node.NodeResolver
import com.badmanners.murglar.lib.core.utils.contract.Model
import java.io.Serializable


/**
 * [Node] metadata parameters.
 *
 * Must be filled in the [NodeResolver].
 */
@Model
data class NodeParameters(

    /**
     * true if this [Node] is non-leaf node - album/playlist/artist/directory/etc, not track.
     */
    val isDirectory: Boolean,

    /**
     * Paging type.
     */
    val pagingType: PagingType,

    /**
     * true if this [Node] contains directory [Node]s, not only track [Node]s.
     */
    val hasSubdirectories: Boolean,

    /**
     * true if this [Node] supports 'likes/dislikes' functionality.
     */
    val isLikeable: Boolean,

    /**
     * [NodeType] of content nodes from searchable node.
     */
    val searchableContentNodeType: String? = null

) : Serializable {

    /**
     * true if this [Node] requires paged content loading.
     */
    val isPageable: Boolean
        get() = pagingType != NON_PAGEABLE

    /**
     * true if this [Node] is unspecified 'search' node
     * and can be passed to [NodeResolver.specifySearchableNode].
     */
    val isSearchable: Boolean
        get() = searchableContentNodeType != null


    /**
     * Types of lists paging.
     */
    enum class PagingType {

        /**
         * List loading by single request.
         */
        NON_PAGEABLE,

        /**
         * List loading by multiple paged requests.
         */
        PAGEABLE,

        /**
         * List loading by multiple paged requests, potentially endless (history/search/etc).
         */
        ENDLESSLY_PAGEABLE
    }


    companion object {
        private const val serialVersionUID = 1L

        fun rootDirectory(pagingType: PagingType, hasSubdirectories: Boolean) =
            directory(pagingType, hasSubdirectories, false)

        fun searchableRootDirectory(
            pagingType: PagingType,
            hasSubdirectories: Boolean,
            searchableContentNodeType: String
        ) = NodeParameters(true, pagingType, hasSubdirectories, false, searchableContentNodeType)

        fun directory(pagingType: PagingType, hasSubdirectories: Boolean, likeable: Boolean) =
            NodeParameters(true, pagingType, hasSubdirectories, likeable)

        fun track(likeable: Boolean) = NodeParameters(false, NON_PAGEABLE, false, likeable)
    }
}