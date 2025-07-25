package com.badmanners.murglar.lib.core.node

import com.badmanners.murglar.lib.core.model.event.Event
import com.badmanners.murglar.lib.core.model.node.NamedPath
import com.badmanners.murglar.lib.core.model.node.Node
import com.badmanners.murglar.lib.core.model.node.NodeParameters
import com.badmanners.murglar.lib.core.model.node.NodeParameters.PagingType
import com.badmanners.murglar.lib.core.model.node.NodeParameters.PagingType.ENDLESSLY_PAGEABLE
import com.badmanners.murglar.lib.core.model.node.NodeParameters.PagingType.NON_PAGEABLE
import com.badmanners.murglar.lib.core.model.node.NodeType
import com.badmanners.murglar.lib.core.model.node.RadioNodeUpdate
import com.badmanners.murglar.lib.core.model.node.Path
import com.badmanners.murglar.lib.core.model.radio.BaseRadio
import com.badmanners.murglar.lib.core.model.radio.RadioSettingsUpdate
import com.badmanners.murglar.lib.core.utils.pattern.PatternMatcher
import kotlin.reflect.KClass


/**
 * Default path segment for entities from urls/related nodes/tracks loaded by media ids.
 */
const val UNMAPPED = "unmapped"


/**
 * Configuration with generic parameters that almost every final configuration have.
 */
sealed interface GenericConfiguration {
    /**
     * Path pattern or exact id/path, by which configuration will be found for node path.
     * @see PatternMatcher.match
     */
    val pattern: String
    val nodeSupplier: NodeSupplier?
    val nodeContentSupplier: NodeContentSupplier?
    val radioContentSupplier: RadioContentSupplier?
    val parameters: NodeParameters
}

/**
 * Configuration with entity-specific parameters.
 * Entities - tracks/albums/artists/playlists/podcasts/podcast episodes/audiobooks/audiobooks parts/radio/etc,
 * not root/intermediate/search directories.
 */
sealed interface EntityConfiguration {
    /**
     * [NodeType] of entity.
     */
    val type: String
    val relatedPaths: RelatedNodePathsGenerator?
    val like: LikeConfig?
    val urlPatterns: List<String>
    val events: List<EventConfig<*>>
}

/**
 * Configuration for root directories - entry points of plugin.
 */
data class Root(
    override val pattern: String,
    val name: () -> String,
    val paging: PagingType,
    val hasSubdirectories: Boolean,
    /**
     * @see NodeResolver.isOwnNode
     */
    val isOwn: Boolean,
    val contentNodeType: String,
    val radioNodeSupplier: RadioNodeSupplier? = null,
    override val nodeContentSupplier: NodeContentSupplier? = null,
    override val radioContentSupplier: RadioContentSupplier? = null
) : GenericConfiguration {
    override val nodeSupplier = null
    override val parameters = NodeParameters.rootDirectory(paging, hasSubdirectories, contentNodeType)
}

/**
 * Configuration for search directories.
 */
data class Search(
    override val pattern: String,
    val name: () -> String,
    val paging: PagingType = ENDLESSLY_PAGEABLE,
    val hasSubdirectories: Boolean,
    val contentNodeType: String,
    override val nodeContentSupplier: NodeContentSupplier
) : GenericConfiguration {
    override val nodeSupplier = null
    override val radioContentSupplier = null
    override val parameters = NodeParameters.searchableRootDirectory(paging, hasSubdirectories, contentNodeType)
}

/**
 * Configuration for intermediate directories.
 */
data class Directory(
    override val pattern: String,
    val paging: PagingType,
    val hasSubdirectories: Boolean,
    val contentNodeType: String,
    override val nodeSupplier: NodeSupplier? = null,
    override val nodeContentSupplier: NodeContentSupplier? = null
) : GenericConfiguration {
    override val radioContentSupplier = null
    override val parameters = NodeParameters.directory(paging, hasSubdirectories, false, contentNodeType)
}

/**
 * Configuration for mapped entities.
 * Mapped here means that node can be resolved by path pattern and has [NodeParameters].
 */
data class MappedEntity(
    override val pattern: String,
    val paging: PagingType,
    val hasSubdirectories: Boolean,
    override val type: String,
    val contentNodeType: String,
    override val relatedPaths: RelatedNodePathsGenerator? = null,
    override val like: LikeConfig? = null,
    override val urlPatterns: List<String> = emptyList(),
    override val events: List<EventConfig<*>> = emptyList(),
    override val nodeSupplier: NodeSupplier? = null,
    override val nodeContentSupplier: NodeContentSupplier? = null,
    override val radioContentSupplier: RadioContentSupplier? = null
) : GenericConfiguration, EntityConfiguration {
    override val parameters = NodeParameters.directory(paging, hasSubdirectories, like != null, contentNodeType)
}

/**
 * Configuration for unmapped entities.
 * Can be used when you want to declare custom related paths/likes/events for unmapped nodes with other [NodeType].
 */
data class UnmappedEntity(
    override val type: String,
    override val relatedPaths: RelatedNodePathsGenerator? = null,
    override val like: LikeConfig? = null,
    override val events: List<EventConfig<*>> = emptyList()
) : GenericConfiguration, EntityConfiguration {
    override val pattern: String = UNMAPPED
    override val urlPatterns: List<String> = emptyList()
    override val nodeSupplier = null
    override val nodeContentSupplier = null
    override val radioContentSupplier = null

    //doesn't matter, just stub, not used
    override val parameters = NodeParameters.directory(NON_PAGEABLE, false, like != null, NodeType.NODE)
}

/**
 * Configuration for tracks.
 * Same as [MappedEntity] + tracks specific fields.
 */
data class Track(
    override val pattern: String,
    override val type: String = NodeType.TRACK,
    override val relatedPaths: RelatedNodePathsGenerator? = null,
    override val like: LikeConfig? = null,
    override val urlPatterns: List<String> = emptyList(),
    override val events: List<EventConfig<*>> = emptyList(),
    override val nodeSupplier: NodeSupplier? = null
) : GenericConfiguration, EntityConfiguration {
    override val nodeContentSupplier = null
    override val radioContentSupplier = null
    override val parameters = NodeParameters.track(like != null)
}

data class LikeConfig(
    val likeListNodePath: Path,
    val likeFunction: LikeFunction
)

data class EventConfig<E : Event>(
    val eventClass: KClass<E>,
    val eventHandler: EventHandler<E>,
)

fun interface RadioNodeSupplier {
    fun createRadioNode(id: String, name: String): BaseRadio
}

/**
 * Supplier of [Node] itself.
 */
fun interface NodeSupplier {
    /**
     * @param parentPath path of parent (relatively to content nodes) node
     * @param params     map with parameters, extracted from path (ids/hashes/etc)
     */
    suspend fun getNode(parentPath: Path, params: Map<String, String>): Node
}

/**
 * Supplier of [Node] content.
 */
fun interface NodeContentSupplier {
    /**
     * @param parentPath path of parent (relatively to content nodes) node
     * @param page       0-based page or null, if [NodeParameters.isPageable] == false
     * @param params     map with parameters, extracted from path (ids/hashes/etc)
     */
    suspend fun getNodeContent(parentPath: Path, page: Int?, params: Map<String, String>): List<Node>
}

/**
 * Supplier of [RadioNodeUpdate].
 */
fun interface RadioContentSupplier {
    /**
     * @param radio radio node for which content is requested
     * @param settingsUpdate radio settings update that reflects user choice,
     *      `null` if settings are unsupported or remains unchanged
     * @param params map with parameters, extracted from path (ids/hashes/etc)
     */
    suspend fun getRadioContent(
        radio: BaseRadio,
        settingsUpdate: RadioSettingsUpdate?,
        params: Map<String, String>
    ): RadioNodeUpdate
}

fun interface LikeFunction {
    suspend fun Node.like(like: Boolean)
}

fun interface EventHandler<E : Event> {
    suspend fun Node.handleEvent(event: E)
}

fun interface RelatedNodePathsGenerator {
    fun Node.generate(): List<NamedPath>
}

fun interface ChildNodePathGenerator<T : Node> {
    fun Path.generate(node: T): Path
}