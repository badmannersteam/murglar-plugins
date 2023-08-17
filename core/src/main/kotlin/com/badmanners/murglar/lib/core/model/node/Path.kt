package com.badmanners.murglar.lib.core.model.node

import com.badmanners.murglar.lib.core.utils.contract.Model
import java.io.Serializable


/**
 * Path representation, consisted of '/' separated segments.
 */
@Model
class Path(val segments: List<String>) : Serializable {

    @Transient
    lateinit var joined: String

    /**
     * Number of path segments.
     */
    val size: Int
        get() = segments.size

    /**
     * True if path is empty (size == 0)
     */
    val isEmpty: Boolean
        get() = size == 0

    /**
     * True if path is complex (has two or more segments).
     */
    val isComplex: Boolean
        get() = size > 1

    /**
     * Returns first path segment.
     *
     * @throws IllegalStateException if path is empty
     */
    val first: String
        get() {
            check(segments.isNotEmpty()) { "Can't get first item of empty path" }
            return segments[0]
        }

    /**
     * Returns last path segment.
     *
     * @throws IllegalStateException if path is empty
     */
    val last: String
        get() {
            check(segments.isNotEmpty()) { "Can't get last item of empty path" }
            return get(segments.size - 1)
        }

    /**
     * Returns specified path segment.
     *
     * @param index segment index (zero-based)
     */
    operator fun get(index: Int): String {
        check(segments.isNotEmpty()) { "Can't get item of empty path" }
        require(index in segments.indices) { "Wrong index $index, when size is ${segments.size}" }
        return segments[index]
    }

    /**
     * Returns subpath of this path.
     *
     * @param start start segment index (zero-based, inclusive)
     * @param end   end segment index (zero-based, exclusive)
     */
    @JvmOverloads
    fun subpath(start: Int = 1, end: Int = size): Path {
        require(start >= 0) { "Start index should be >= 0. Got: $start" }
        require(end <= segments.size) { "End index should be <= than path size. Got: $end" }
        require(start <= end) { "Start index should be <= than end index. Got: start $start, end $end" }
        return Path(segments.subList(start, end))
    }

    /**
     * Returns parent path (a path without last segment).
     *
     * For example if source path is 'grand.parent.name' then this method return 'grand.parent'
     *
     * @return Path parent path.
     */
    fun parent(): Path = subpath(0, segments.size - 1)

    /**
     * Returns child path.
     *
     * For example if source path is 'grand.parent' then following code return 'grand.parent.name':
     * ```
     * child('name')
     * ```
     *
     * @return Path child path
     */
    fun child(segment: String): Path = Path(segments + segment)

    /**
     * Returns sibling path (a path to segment on the same level as current)
     *
     * For example if source path is `grand.parent.name` then following code return `grand.parent.surname`:
     * ```
     * sibling('surname')
     * ```
     */
    fun sibling(segment: String): Path = parent().child(segment)

    /**
     * Returns path with appendix, directly appended to the last segment.
     *
     * For example if source path is 'grand.child' then following code return 'grand.child-name':
     * ```
     * appendToLast('-name')
     * ```
     *
     * @return Path appended path
     */
    fun appendToLast(appendix: String): Path {
        val segments = segments.toMutableList()
        val last = segments.size - 1
        segments[last] = segments[last] + appendix
        return Path(segments)
    }


    override fun toString(): String {
        if (!::joined.isInitialized)
            joined = segments.joinToString(SEPARATOR)
        return joined
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false
        other as Path
        return segments == other.segments
    }

    override fun hashCode(): Int = segments.hashCode()


    companion object {

        private const val serialVersionUID = 1L

        const val SEPARATOR = "/"

        @JvmStatic
        val EMPTY = Path(emptyList())

        fun empty() = EMPTY

        /**
         * Parses '/' separated string to path.
         */
        fun parse(string: String?): Path = string?.split(SEPARATOR)?.let(::Path) ?: EMPTY
    }
}