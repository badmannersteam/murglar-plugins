package com.badmanners.murglar.lib.core.service.enhancer

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.model.track.source.Source


interface Enhancer {

    val serviceId: String

    fun canGetTrackUrl(track: BaseTrack, source: Source): Boolean

    suspend fun getTrackUrl(track: BaseTrack, source: Source): String
}