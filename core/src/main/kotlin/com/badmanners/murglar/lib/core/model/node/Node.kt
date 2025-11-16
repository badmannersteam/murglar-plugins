package com.badmanners.murglar.lib.core.model.node

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.node.NodeResolver
import com.badmanners.murglar.lib.core.service.Murglar
import com.badmanners.murglar.lib.core.utils.contract.Model
import java.io.Serializable


/**
 * Node.
 *
 * Used to represent hierarchical/filesystem style tree of nodes.
 *
 * Nodes can be inner nodes (albums/playlists/artists/directories/radios/etc) and leafs (tracks).
 *
 * All implementations of this interface MUST have 0-args constructors (private constructor is enough) - see [Model].
 * It is necessary for hassle-free (de)serialization on the client side.
 *
 * @see NodeParameters
 * @see Path
 * @see NodeResolver
 */
interface Node : Serializable {

    /**
     * Unique per [Murglar] node id.
     */
    val nodeId: String

    /**
     * Node name (song title/artist name/directory name/etc).
     */
    val nodeName: String

    /**
     * Node summary, second line text (song artist/album version/etc)
     */
    val nodeSummary: String?

    /**
     * Type of node (album/artist/track/playlist/etc).
     *
     * @see NodeType
     */
    val nodeType: String

    /**
     * Comparable id of node, by this id two nodes will be compared in the "likes/dislikes" functionality.
     *
     * Should be overridden only if [nodeId] of two 'equals' nodes in the service are not equal,
     * but this nodes must be treated as equal in the "likes/dislikes" functionality.
     */
    val comparableId: String

    /**
     * Url of the small cover, if exists.
     */
    val smallCoverUrl: String?

    /**
     * Url of the big cover, if exists.
     */
    val bigCoverUrl: String?

    /**
     * Url of the node in the music service.
     */
    val serviceUrl: String?

    /**
     * [NodeParameters] of this node.
     * @see NodeResolver
     */
    val nodeParameters: NodeParameters

    /**
     * [Path] of this node.
     * @see NodeResolver
     */
    val nodePath: Path


    companion object {

        /**
         * true if node has any cover (at least small), false otherwise.
         */
        val Node.hasCover: Boolean
            get() = !smallCoverUrl.isNullOrEmpty() || !bigCoverUrl.isNullOrEmpty()

        /**
         * Url of the best (biggest) cover, if exists.
         */
        val Node.bestCoverUrl: String?
            get() = when {
                !bigCoverUrl.isNullOrEmpty() -> bigCoverUrl
                !smallCoverUrl.isNullOrEmpty() -> smallCoverUrl
                else -> null
            }

        /**
         * true if node has url to its music service, false otherwise.
         */
        val Node.hasServiceUrl: Boolean
            get() = !serviceUrl.isNullOrEmpty()

        /**
         * Converts track node to the [BaseTrack] subclass.
         *
         * @throws IllegalArgumentException if node is not a track
         */
        inline fun <reified T : BaseTrack> Node.toTrack(): T = to<T>()

        /**
         * Converts node to the [Node] subclass.
         *
         * @throws IllegalArgumentException if node can't be cast
         */
        inline fun <reified T : Node> Node.to(): T {
            require(T::class.java.isAssignableFrom(javaClass)) {
                "Node '$nodePath' is '${javaClass.name}', not a '${T::class.java.name}'!"
            }
            return this as T
        }

        /**
         * Converts track node to the [BaseTrack] subclass (non reified/inline/extension Java version).
         *
         * @throws IllegalArgumentException if node is not a track
         */
        @Suppress("UNCHECKED_CAST")
        fun <T : BaseTrack> Node.toTrackJ(): T {
            require(this is BaseTrack) { "Node '$nodePath' is not a track!" }
            return this as T
        }

        /**
         * Converts node to the [Node] subclass (non reified/inline/extension Java version).
         *
         * @throws IllegalArgumentException if node can't be cast to clazz
         */
        @Suppress("UNCHECKED_CAST")
        fun <T : Node> Node.to(clazz: Class<T>): T {
            require(clazz.isAssignableFrom(javaClass)) {
                "Node '$nodePath' is '${javaClass.name}', not a '${clazz.name}'!"
            }
            return this as T
        }
    }
}