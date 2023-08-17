package com.badmanners.murglar.lib.core.service.enhancer

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.model.track.source.Source
import com.badmanners.murglar.lib.core.utils.contract.WorkerThread


interface EnhancerDzr {

    val canGetTrackUrl: Boolean

    @WorkerThread
    fun getTrackUrl(track: BaseTrack, source: Source): String
}