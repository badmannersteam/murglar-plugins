package com.badmanners.murglar.lib.core.service.enhancer

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.model.track.source.Source


interface EnhancerDzr {

    val canGetTrackUrl: Boolean

    suspend fun getTrackUrl(track: BaseTrack, source: Source): String
}