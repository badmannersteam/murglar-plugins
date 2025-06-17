package com.badmanners.murglar.lib.core.node

import com.badmanners.murglar.lib.core.model.event.Event
import com.badmanners.murglar.lib.core.model.event.NamedAction
import com.badmanners.murglar.lib.core.model.node.NamedPath
import com.badmanners.murglar.lib.core.model.node.Node
import com.badmanners.murglar.lib.core.model.node.Path
import com.badmanners.murglar.lib.core.model.track.BaseTrack
import kotlin.reflect.KClass


/**
 * Stub impl of [NodeSupplier].
 */
class StubNodeResolver(
    override val rootPath: Path
) : NodeResolver {

    override val isAvailable = false

    override fun isNodeOwner(path: Path) = false

    override fun getRootNodes(onlyAvailable: Boolean) = emptyList<Node>()

    override suspend fun getNode(path: Path) = throw UnsupportedOperationException("'getNode' in stub node resolver")

    override fun getNodeParameters(path: Path) =
        throw UnsupportedOperationException("'getNodeParameters' in stub node resolver")

    override fun getRelatedNodePaths(node: Node): List<NamedPath> =
        throw UnsupportedOperationException("'getRelatedNodes' in stub node resolver")

    override fun isNodeCacheable(path: Path) =
        throw UnsupportedOperationException("'isNodeCacheable' in stub node resolver")

    override fun isOwnNode(path: Path) =
        throw UnsupportedOperationException("'isOwnNode' in stub node resolver")

    override fun specifySearchableNode(searchableNode: Node, query: String) =
        throw UnsupportedOperationException("'specifySearchableNode' in stub node resolver")

    override suspend fun getNodeContent(path: Path, page: Int?) =
        throw UnsupportedOperationException("'getNodeContent' in stub node resolver")

    override suspend fun getRadioContent(radioNode: Node) =
        throw UnsupportedOperationException("'getRadioContent' in stub node resolver")

    override val supportsLikes = false

    override val likesMapping get() = throw UnsupportedOperationException("'likesMapping' in stub node resolver")

    override suspend fun likeNode(node: Node, like: Boolean) =
        throw UnsupportedOperationException("'likeNode' in stub node resolver")

    override fun canGetNodeFromUrl(url: String) = false

    override suspend fun getNodeFromUrl(url: String) =
        throw UnsupportedOperationException("'getNodeFromUrl' in stub node resolver")

    override fun getNodeCustomActions(node: Node): List<NamedAction> = emptyList()

    override val supportedEventsMapping = emptyMap<String, Set<KClass<out Event>>>()

    override suspend fun handleEvent(event: Event, node: Node) =
        throw UnsupportedOperationException("Event handling not supported")

    override suspend fun getTracksByMediaIds(mediaIds: List<String>): List<BaseTrack> = emptyList()
}