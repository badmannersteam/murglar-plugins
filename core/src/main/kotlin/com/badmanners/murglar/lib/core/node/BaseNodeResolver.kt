package com.badmanners.murglar.lib.core.node

import com.badmanners.murglar.lib.core.localization.Messages
import com.badmanners.murglar.lib.core.model.event.Event
import com.badmanners.murglar.lib.core.model.event.NamedAction
import com.badmanners.murglar.lib.core.model.node.BaseNode
import com.badmanners.murglar.lib.core.model.node.MutableNode
import com.badmanners.murglar.lib.core.model.node.NamedPath
import com.badmanners.murglar.lib.core.model.node.Node
import com.badmanners.murglar.lib.core.model.node.NodeParameters
import com.badmanners.murglar.lib.core.model.node.NodeParameters.PagingType.ENDLESSLY_PAGEABLE
import com.badmanners.murglar.lib.core.model.node.NodeType
import com.badmanners.murglar.lib.core.model.node.NodeType.ALBUM
import com.badmanners.murglar.lib.core.model.node.NodeType.ARTIST
import com.badmanners.murglar.lib.core.model.node.NodeType.PLAYLIST
import com.badmanners.murglar.lib.core.model.node.NodeType.TRACK
import com.badmanners.murglar.lib.core.model.node.NodeWithContent
import com.badmanners.murglar.lib.core.model.node.Path
import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.node.BaseNodeResolver.InternalConfiguration.EventHandlerConfiguration
import com.badmanners.murglar.lib.core.node.BaseNodeResolver.InternalConfiguration.LikeConfiguration
import com.badmanners.murglar.lib.core.node.BaseNodeResolver.InternalConfiguration.ParametersConfiguration
import com.badmanners.murglar.lib.core.node.BaseNodeResolver.InternalConfiguration.RelatedNodeConfiguration
import com.badmanners.murglar.lib.core.node.BaseNodeResolver.InternalConfiguration.SuppliersConfiguration
import com.badmanners.murglar.lib.core.node.BaseNodeResolver.InternalConfiguration.UrlConfiguration
import com.badmanners.murglar.lib.core.service.Murglar
import com.badmanners.murglar.lib.core.utils.MurglarLibUtils.urlDecode
import com.badmanners.murglar.lib.core.utils.MurglarLibUtils.urlEncode
import com.badmanners.murglar.lib.core.utils.pattern.PatternMatcher.matchAllFromCollection
import com.badmanners.murglar.lib.core.utils.pattern.PatternMatcher.matchFromCollection
import kotlin.reflect.KClass


/**
 * Base abstract [NodeResolver] impl.
 *
 * Handles most parts of internal [NodeResolver] logic,
 * leaving to inheritors only declarations of the [GenericConfiguration]s (see [configurations]),
 * that calls methods from [Murglar].
 *
 * If required inheritor can override any method from [NodeResolver] to override default logic (e.g. custom url parsing).
 *
 * When default implementation searches for appropriate configuration it follows next rules
 * (method : searches by field -> used configuration field):
 * * getNodeParameters   : pattern    -> parameters
 * * getNode             : pattern    -> nodeSupplier
 * * getNodeContent      : pattern    -> nodeContentSupplier
 * * getRadioContent     : pattern    -> nodeWithContentSupplier
 * * getRelatedNodePaths : nodeType   -> relatedNodes.pathsGenerator
 * * likeNode            : nodeType   -> likes.likeFunction
 * * handleEvent         : nodeType   -> eventHandler
 * * getNodeFromUrl      : urlPattern -> nodeSupplier
 */
abstract class BaseNodeResolver<M : Murglar<*>, ME : Messages>(
    protected val murglar: M,
    protected val messages: ME
) : NodeResolver {

    /**
     * List of configurations, provided by implementation.
     */
    protected abstract val configurations: List<GenericConfiguration>


    protected val config: InternalConfiguration by lazy {

        val parameters = mutableListOf<ParametersConfiguration>()
        val suppliers = mutableListOf<SuppliersConfiguration>()
        val relatedNodes = mutableListOf<RelatedNodeConfiguration>()
        val likes = mutableListOf<LikeConfiguration>()
        val eventHandlers = mutableListOf<EventHandlerConfiguration>()
        val urls = mutableListOf<UrlConfiguration>()
        val ownRoots = mutableSetOf<String>()

        configurations.forEach {
            val pattern = it.pattern
            val nodeSupplier = it.nodeSupplier
            val nodeContentSupplier = it.nodeContentSupplier
            val nodeWithContentSupplier = it.nodeWithContentSupplier

            val hasAtLeastOneSupplier =
                nodeSupplier != null || nodeContentSupplier != null || nodeWithContentSupplier != null

            check(nodeWithContentSupplier == null || it.parameters.pagingType == ENDLESSLY_PAGEABLE) {
                "Radio must have '$ENDLESSLY_PAGEABLE' paging!"
            }

            if (it !is UnmappedEntity)
                parameters += ParametersConfiguration(pattern, it.parameters)

            if (it !is Search && it !is UnmappedEntity && hasAtLeastOneSupplier)
                suppliers += SuppliersConfiguration(pattern, nodeSupplier, nodeContentSupplier, nodeWithContentSupplier)

            when (it) {
                is Search -> {
                    val queryAwareSearchNodePattern = "${pattern}_query-<query>"
                    parameters += ParametersConfiguration(
                        pattern = queryAwareSearchNodePattern,
                        parameters = it.parameters.copy(searchableContentNodeType = null)
                    )
                    suppliers += SuppliersConfiguration(
                        pattern = queryAwareSearchNodePattern,
                        nodeContentSupplier = nodeContentSupplier
                    )
                }

                is EntityConfiguration -> {
                    val type = it.type
                    it.relatedPaths?.let { generator ->
                        relatedNodes += RelatedNodeConfiguration(type, generator)
                    }
                    it.like?.let { config ->
                        likes += LikeConfiguration(type, config.likeListNodePath, config.likeFunction)
                    }
                    it.events.forEach { config ->
                        val eventClass = config.eventClass
                        //TODO requires kotlin-reflect
                        /*val isPlayerEvent = eventClass.isSubclassOf(PlayerEvent::class)
                        val isCustomAction = eventClass.isSubclassOf(CustomAction::class)
                        check((isPlayerEvent && type == TRACK) || isCustomAction) {
                            "Wrong event class $eventClass in $it!"
                        }*/
                        eventHandlers +=
                            EventHandlerConfiguration(type, eventClass, config.eventHandler as EventHandler<Event>)
                    }
                    it.urlPatterns.forEach { urlPattern ->
                        checkNotNull(nodeSupplier) { "urlPatterns presented, but no nodeSupplier in $it!" }
                        urls += UrlConfiguration(urlPattern, nodeSupplier)
                    }
                }

                else -> {}
            }

            if (it is Root && it.isOwn)
                ownRoots += pattern
        }

        InternalConfiguration(parameters, suppliers, relatedNodes, likes, eventHandlers, urls, ownRoots)
    }


    override val isAvailable: Boolean get() = murglar.loginResolver.isLogged

    override val rootPath: Path = Path.parse(murglar.id)

    override fun isNodeOwner(path: Path) = path.first == murglar.id

    override fun getRootNodes(onlyAvailable: Boolean): List<Node> = when {
        onlyAvailable && !isAvailable -> emptyList()
        else -> configurations.asSequence()
            .filter { it is Root || it is Search }
            .map {
                val name = when (it) {
                    is Root -> it.name()
                    is Search -> it.name()
                    else -> error("Unknown configuration type ${it.javaClass}!")
                }
                when {
                    it is Root && it.rootNodeSupplier != null ->
                        rootNode(it.rootNodeSupplier.createRootNode(it.pattern, name))

                    else -> rootNode(it.pattern, name)
                }
            }
            .toList()
    }

    override suspend fun getNode(path: Path): Node {
        requireBelongsToThisResolver(path)

        val subPath = path.subpath().toString()
        return matchFromCollection(subPath, config.suppliers, { it.pattern }, { it.nodeSupplier })
            ?.let {
                val nodeSupplier = checkNotNull(it.value) {
                    "Configuration '${it.pattern}' for '$path' found, but has no nodeSupplier!"
                }
                nodeSupplier.getNode(path.parent(), it.parameters)
            } ?: error("No configuration found for '$path'")
    }

    override fun getNodeParameters(path: Path): NodeParameters {
        requireBelongsToThisResolver(path)

        val subPath = path.subpath().toString()
        return matchFromCollection(subPath, config.parameters, { it.pattern }, { it.parameters })
            ?.value
            ?: error("No configuration found for '$path'")
    }

    override fun getRelatedNodePaths(node: Node): List<NamedPath> = config.relatedNodes
        .firstOrNull { it.nodeType == node.nodeType }
        ?.pathsGenerator
        ?.run { node.generate() }
        ?: emptyList()

    override fun isNodeCacheable(path: Path): Boolean {
        if (path.size < 2)
            return true

        val segment = path[1].lowercase()
        return segment != UNMAPPED && !segment.contains("search")
    }

    override fun isOwnNode(path: Path): Boolean {
        requireBelongsToThisResolver(path)
        return path.size > 1 && path[1] in config.ownRoots
    }

    override fun specifySearchableNode(searchableNode: Node, query: String): Node {
        requireBelongsToThisResolver(searchableNode.nodePath)
        val parameters = searchableNode.nodeParameters
        val isSearchable = parameters.isSearchable
        val isDirectory = parameters.isDirectory
        require(isSearchable && isDirectory && query.isNotEmpty()) {
            "Invalid searchable node or query: isDirectory - '$isDirectory', isSearchable - '$isSearchable', query - '$query'"
        }

        val encodedQuery = query.urlEncode()
        val newNodeId = "${searchableNode.nodeId}_$encodedQuery"
        val newName = "${searchableNode.nodeName}: $query"
        val newPath = searchableNode.nodePath.appendToLast("_query-$encodedQuery")

        return node(newNodeId, newName, newPath)
    }

    /**
     * Extracts search query from parameters map or throws.
     */
    protected fun Map<String, String>.getQuery(): String = this["query"]?.urlDecode()
        ?: error("No query found in params!")

    override suspend fun getNodeContent(path: Path, page: Int?): List<Node> {
        requireBelongsToThisResolver(path)

        val isPageable = getNodeParameters(path).isPageable
        require(!isPageable || page != null && page >= 0) {
            "Invalid paging parameters: isPageable - '$isPageable', page - '$page'"
        }

        val subPath = path.subpath().toString()
        return matchFromCollection(subPath, config.suppliers, { it.pattern }, { it.nodeContentSupplier })
            ?.let {
                val nodeContentSupplier = checkNotNull(it.value) {
                    "Configuration '${it.pattern}' for '$path' found, but has no nodeContentSupplier!"
                }
                nodeContentSupplier.getNodeContent(path, page, it.parameters)
            } ?: error("No configuration found for '$path'")
    }

    override suspend fun getRadioContent(radioNode: Node): NodeWithContent {
        val path = radioNode.nodePath

        requireBelongsToThisResolver(path)
        require(NodeType.RADIO == radioNode.nodeType) { "Invalid node type: '${radioNode.nodeType}'" }

        val subPath = path.subpath().toString()
        return matchFromCollection(subPath, config.suppliers, { it.pattern }, { it.nodeWithContentSupplier })
            ?.let {
                val nodeWithContentSupplier = checkNotNull(it.value) {
                    "Configuration '${it.pattern}' for '$path' found, but has no nodeWithContentSupplier!"
                }
                nodeWithContentSupplier.getNodeWithContent(radioNode, it.parameters)
            } ?: error("No configuration found for '$path'")
    }


    override val supportsLikes get() = isAvailable && config.likes.isNotEmpty()

    override val likesMapping get() = config.likes.associate { it.nodeType to it.likeListNodePath }

    override suspend fun likeNode(node: Node, like: Boolean) {
        requireBelongsToThisResolver(node.nodePath)
        check(supportsLikes) { "Likes aren't supported!" }

        val likeConfiguration = config.likes.firstOrNull { it.nodeType == node.nodeType }
            ?: error("No like configuration found for ${node.nodeType}!")

        likeConfiguration.likeFunction.run { node.like(like) }
    }

    override fun canGetNodeFromUrl(url: String): Boolean = matchFromCollection(
        url, config.urls, { it.urlPattern }, { it.nodeSupplier }) != null

    override suspend fun getNodeFromUrl(url: String): Node {
        val results = matchAllFromCollection(url, config.urls, { it.urlPattern }, { it.nodeSupplier })
            .map { runCatching { it.value.getNode(unmappedPath(), it.parameters) } }
            .groupBy { it.isSuccess }
        return when {
            !results[true].isNullOrEmpty() -> results[true]!!.first().getOrThrow()
            !results[false].isNullOrEmpty() -> throw results[false]!!.first().exceptionOrNull()!!
            else -> error("Unsupported url: '$url'")
        }
    }


    override fun getNodeCustomActions(node: Node): List<NamedAction> = emptyList()

    override val supportedEventsMapping: Map<String, Set<KClass<out Event>>>
        get() = if (isAvailable)
            config.eventHandlers.groupBy({ it.nodeType }, { it.eventClass }).mapValues { it.value.toSet() }
        else
            emptyMap()

    override suspend fun handleEvent(event: Event, node: Node) {
        requireBelongsToThisResolver(node.nodePath)
        require(isAvailable)

        val eventHandlerConfiguration = config.eventHandlers.firstOrNull {
            it.nodeType == node.nodeType && it.eventClass == event::class
        } ?: error("No events handling configuration found for ${node.nodeType} and ${event::class.simpleName}!")

        eventHandlerConfiguration.eventHandler.run { node.handleEvent(event) }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun getTracksByMediaIds(mediaIds: List<String>): List<BaseTrack> =
        murglar.getTracksByMediaIds(mediaIds).convertTracks(unmappedPath()) as List<BaseTrack>


    protected fun <N : MutableNode> List<N>.convertTracks(parentPath: Path) = convert(TRACK, parentPath)

    protected fun <N : MutableNode> List<N>.convertAlbums(parentPath: Path) = convert(ALBUM, parentPath)

    protected fun <N : MutableNode> List<N>.convertArtists(parentPath: Path) = convert(ARTIST, parentPath)

    protected fun <N : MutableNode> List<N>.convertPlaylists(parentPath: Path) = convert(PLAYLIST, parentPath)

    /**
     * Fills nodes [NodeParameters] and [Path] according to passed args and [ParametersConfiguration]s.
     */
    protected fun <N : MutableNode> List<N>.convert(nodeTag: String, parentPath: Path) =
        convert({ child(nodeTag, it) }, parentPath)

    /**
     * Fills nodes [NodeParameters] and [Path] according to passed args and [ParametersConfiguration]s.
     */
    protected fun <N : MutableNode> List<N>.convert(pathGenerator: ChildNodePathGenerator<N>, parentPath: Path) =
        map { it.convert(pathGenerator, parentPath) }

    protected fun <N : MutableNode> N.convertTrack(parentPath: Path) = convert(TRACK, parentPath)

    protected fun <N : MutableNode> N.convertAlbum(parentPath: Path) = convert(ALBUM, parentPath)

    protected fun <N : MutableNode> N.convertArtist(parentPath: Path) = convert(ARTIST, parentPath)

    protected fun <N : MutableNode> N.convertPlaylist(parentPath: Path) = convert(PLAYLIST, parentPath)

    /**
     * Fills node [NodeParameters] and [Path] according to passed args and [ParametersConfiguration]s.
     */
    protected fun <N : MutableNode> N.convert(nodeTag: String, parentPath: Path) =
        convert({ child(nodeTag, it) }, parentPath)

    /**
     * Fills node [NodeParameters] and [Path] according to passed args and [ParametersConfiguration]s.
     */
    protected fun <N : MutableNode> N.convert(pathGenerator: ChildNodePathGenerator<N>, parentPath: Path): Node =
        pathGenerator.run {
            parentPath.generate(this@convert).let {
                withNodePath(it)
                withNodeParameters(getNodeParameters(it))
            }
        }


    protected fun rootNode(nodeId: String, name: String) = node(nodeId, name, rootNodePath(nodeId))

    protected fun rootNode(node: MutableNode) = node(node, rootNodePath(node.nodeId))

    /**
     * Returns subdirectory [Node], filled with [NodeParameters] and [Path].
     */
    protected fun subdirectoryNode(nodeId: String, name: String, parentPath: Path) =
        node(nodeId, name, parentPath.child(nodeId))

    /**
     * Returns subdirectory [Node], filled with [NodeParameters] and [Path].
     */
    protected fun subdirectoryNode(node: MutableNode, parentPath: Path) = node(node, parentPath.child(node.nodeId))

    protected fun node(nodeId: String, name: String, path: Path) = node(BaseNode(nodeId, name), path)

    protected fun node(node: MutableNode, path: Path): Node = node.apply {
        withNodePath(path)
        withNodeParameters(getNodeParameters(path))
    }


    /**
     * Path of [UNMAPPED] segment.
     * Can be used as parent path for entities from urls/related nodes/tracks loaded by media ids.
     */
    protected fun unmappedPath() = rootNodePath(UNMAPPED)

    /**
     * Returns root path of node with passed node id.
     */
    protected fun rootNodePath(nodeId: String) = rootPath.child(nodeId)

    protected fun Path.child(nodeTag: String, node: Node) = child("$nodeTag-${node.nodeId}")


    protected fun requireBelongsToThisResolver(path: Path) = require(isNodeOwner(path)) {
        "Node '$path' doesn't belong to this resolver!"
    }


    protected data class InternalConfiguration(
        val parameters: List<ParametersConfiguration>,
        val suppliers: List<SuppliersConfiguration>,
        val relatedNodes: List<RelatedNodeConfiguration>,
        val likes: List<LikeConfiguration>,
        val eventHandlers: List<EventHandlerConfiguration>,
        val urls: List<UrlConfiguration>,
        val ownRoots: Set<String>
    ) {
        data class ParametersConfiguration(
            val pattern: String,
            val parameters: NodeParameters
        )

        data class SuppliersConfiguration(
            val pattern: String,
            val nodeSupplier: NodeSupplier? = null,
            val nodeContentSupplier: NodeContentSupplier? = null,
            val nodeWithContentSupplier: NodeWithContentSupplier? = null
        ) {
            init {
                require(nodeSupplier != null || nodeContentSupplier != null || nodeWithContentSupplier != null) {
                    "No supplier specified!"
                }
            }
        }

        data class RelatedNodeConfiguration(
            val nodeType: String,
            val pathsGenerator: RelatedNodePathsGenerator
        )

        data class LikeConfiguration(
            val nodeType: String,
            val likeListNodePath: Path,
            val likeFunction: LikeFunction
        )

        data class UrlConfiguration(
            val urlPattern: String,
            val nodeSupplier: NodeSupplier
        )

        data class EventHandlerConfiguration(
            val nodeType: String,
            val eventClass: KClass<out Event>,
            val eventHandler: EventHandler<Event>
        )
    }
}