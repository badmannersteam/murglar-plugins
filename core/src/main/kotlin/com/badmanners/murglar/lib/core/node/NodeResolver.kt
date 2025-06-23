package com.badmanners.murglar.lib.core.node

import com.badmanners.murglar.lib.core.model.event.Event
import com.badmanners.murglar.lib.core.model.event.NamedAction
import com.badmanners.murglar.lib.core.model.node.NamedPath
import com.badmanners.murglar.lib.core.model.node.Node
import com.badmanners.murglar.lib.core.model.node.NodeParameters
import com.badmanners.murglar.lib.core.model.node.NodeType
import com.badmanners.murglar.lib.core.model.node.RadioNodeUpdate
import com.badmanners.murglar.lib.core.model.node.Path
import com.badmanners.murglar.lib.core.model.radio.BaseRadio
import com.badmanners.murglar.lib.core.model.radio.RadioSettingsUpdate
import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.service.Murglar
import com.badmanners.murglar.lib.core.utils.MediaId
import kotlin.reflect.KClass


/**
 * Node resolver.
 *
 * Provides the ability to use [Murglar] in generic, filesystem like style:
 *  * query nodes and their content
 *  * prepare searchable nodes
 *  * manage likes/dislikes
 *  * convert web urls from music services to nodes
 *  * ...
 *
 * As a base for implementations use [BaseNodeResolver].
 *
 * As a temporary stub [StubNodeResolver] can be used.
 *
 * @see Node
 * @see NodeParameters
 * @see Path
 */
interface NodeResolver {

    /**
     * True, if this resolver is ready for work (logged in/etc).
     */
    val isAvailable: Boolean

    /**
     * Root path of this resolver.
     */
    val rootPath: Path

    /**
     * Returns true, if this resolver can handle [Node] with requested [Path].
     */
    fun isNodeOwner(path: Path): Boolean

    /**
     * Returns all root [Node]s, that this resolver supports, if [onlyAvailable]==false
     * (including the nodes, which resolver can't proceed now for some reason, for example because user isn't logged in),
     * or only available now [Node]s, if onlyAvailable==true.
     *
     * TODO: rework, [NodeResolver.isAvailable] means not the same as [onlyAvailable] here.
     */
    fun getRootNodes(onlyAvailable: Boolean): List<Node>

    /**
     * Returns [Node] with requested [Path].
     */
    suspend fun getNode(path: Path): Node

    /**
     * Returns [NodeParameters] for the [Node] with requested [Path].
     *
     * @throws IllegalArgumentException if path is unknown
     */
    fun getNodeParameters(path: Path): NodeParameters

    /**
     * Returns list of the named paths to the related node (e.g. paths to the artist and album nodes for track node).
     */
    fun getRelatedNodePaths(node: Node): List<NamedPath>

    /**
     * Returns true, if [Node] along passed [Path] could be cached on the resolver client's side.
     *
     * TODO: rework this.
     */
    fun isNodeCacheable(path: Path): Boolean

    /**
     * Returns true, if [Node] along passed [Path] is an own node - e.g. from the user own library,
     * not from search/recommendations/radio/etc.
     */
    fun isOwnNode(path: Path): Boolean

    /**
     * Converts searchable [Node] and query to the content [Node],
     * which can be passed to the [getNodeContent].
     *
     * @param searchableNode [Node], with [NodeParameters.isSearchable]==true.
     * @param query          user inputted query
     * @return non-searchable content [Node].
     * @throws IllegalArgumentException if node is not searchable or query is empty
     */
    fun specifySearchableNode(searchableNode: Node, query: String): Node

    /**
     * Returns content (list of child [Node]s) for node with requested [Path].
     *
     * @param path path of the [Node].
     * @param page 0-based page if [NodeParameters.isPageable] or null otherwise
     */
    suspend fun getNodeContent(path: Path, page: Int? = null): List<Node>

    /**
     * Returns radio update - next part of radio tracks and updated radio node.
     *
     * @param radio radio node from the nodes tree or updated radio node from the previous call of this method.
     * @param settingsUpdate radio settings update that reflects user choice,
     *      `null` if settings are unsupported or remains unchanged.
     */
    suspend fun getRadioContent(radio: BaseRadio, settingsUpdate: RadioSettingsUpdate?): RadioNodeUpdate

    /**
     * Returns true if this resolver supports likes/dislikes right now
     * (resolver has like handlers, user is logged and etc.), false otherwise
     */
    val supportsLikes: Boolean

    /**
     * Returns 'node type' -> 'root node path' mapping for likes lists.
     *
     * Determining the like status for any node:
     *  * get mapping from this method
     *  * get (and cache) content of all roots, listed in mapping
     *  * for every node call [Node.nodeType] and find a corresponding root node from mapping
     *  * check that found list contains or not the [Node] with the equal [Node.comparableId]
     *  * if contains - node already liked, if not - node can be liked
     *
     *
     * @see NodeType
     */
    val likesMapping: Map<String, Path>

    /**
     * Performs like/dislike action.
     *
     * @param node node which must be liked/disliked
     * @param like true for like, false for dislike
     */
    suspend fun likeNode(node: Node, like: Boolean)

    /**
     * Manager for playlists management.
     *
     * Not null if management is supported.
     */
    val playlistsManager: PlaylistsManager?

    /**
     * Checks that this resolver most likely can load [Node] for the requested url.
     *
     * @param url url to the music service
     */
    fun canGetNodeFromUrl(url: String): Boolean

    /**
     * Attempts to load [Node] for the requested url.
     * Url must be checked with the [NodeResolver.canGetNodeFromUrl] before.
     *
     * @param url url to the music service
     */
    suspend fun getNodeFromUrl(url: String): Node

    /**
     * Returns all supported [NamedAction]s for passed [Node].
     */
    fun getNodeCustomActions(node: Node): List<NamedAction>

    /**
     * Returns 'node type' -> 'set of the supported event types' mapping.
     * Includes only events, that can be handled right now.
     */
    val supportedEventsMapping: Map<String, Set<KClass<out Event>>>

    /**
     * Performs event handling.
     *
     * @param event event
     * @param node node, related to event
     * @throws UnsupportedOperationException if event handling not supported
     * @see Event
     */
    suspend fun handleEvent(event: Event, node: Node)

    /**
     * Returns all possible tracks by media ids.
     * Used by the clients for re-requesting tracks metadata.
     *
     * @param mediaIds list of [BaseTrack.mediaId]s
     * @see MediaId
     */
    suspend fun getTracksByMediaIds(mediaIds: List<String>): List<BaseTrack>
}