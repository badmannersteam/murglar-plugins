package com.badmanners.murglar.lib.core.utils

import com.badmanners.murglar.lib.core.service.Murglar


/**
 * Helper for building/parsing media id.
 *
 * Media id format is
 * ```
 * murglarId_id1_id2_..._idN
 * ```
 * where id1-idN are user provided custom ids, that uniquely identify track in the music service
 * (e.g. ownerId + trackId or just single trackId).
 */
object MediaId {

    /**
     * Builds string representation of [MediaId].
     * @param murglarId id of murglar, MUST be constant (XxxMurglar.SERVICE_ID), not [Murglar.id]!
     * @param customIds custom ids, that uniquely identify track in the music service
     * (e.g. ownerId + trackId or just single trackId).
     */
    fun build(murglarId: String, vararg customIds: String) = sequenceOf(murglarId, *customIds).joinToString("_")

    /**
     * Parses list of custom ids from [MediaId] string representation.
     */
    fun getIds(mediaId: String) = mediaId.split("_").drop(1)
}