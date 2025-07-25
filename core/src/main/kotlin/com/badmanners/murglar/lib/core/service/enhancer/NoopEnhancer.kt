package com.badmanners.murglar.lib.core.service.enhancer

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.model.track.source.Source


class NoopEnhancer(override val serviceId: String) : Enhancer {

    override fun canGetTrackUrl(track: BaseTrack, source: Source) = false

    override suspend fun getTrackUrl(track: BaseTrack, source: Source) =
        throw UnsupportedOperationException("getTrackUrl")
}
