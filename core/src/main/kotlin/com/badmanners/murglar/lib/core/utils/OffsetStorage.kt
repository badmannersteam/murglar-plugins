package com.badmanners.murglar.lib.core.utils


/**
 * Storage for dynamic paging offsets.
 * Helps to maintain conversion between page number and custom offset/cursor.
 */
class OffsetStorage(
    private val startOffset: String
) {

    private val offsets: MutableMap<String, MutableMap<Int, String>> = HashMap()

    fun save(id: String, currentPage: Int, nextOffset: String) {
        offsets.getOrPut(id, ::HashMap)[currentPage + 1] = nextOffset
    }

    fun has(id: String, page: Int): Boolean = get(id, page) != null

    fun get(id: String, page: Int): String? = when (page) {
        0 -> startOffset
        else -> offsets[id]?.get(page)
    }
}