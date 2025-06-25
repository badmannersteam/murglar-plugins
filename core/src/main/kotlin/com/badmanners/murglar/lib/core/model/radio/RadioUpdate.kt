package com.badmanners.murglar.lib.core.model.radio

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.utils.contract.Model


/**
 * Batch of radio and next tracks.
 */
@Model
class RadioUpdate<R : BaseRadio, T : BaseTrack>(
    val updatedRadio: R,
    val nextTracks: List<T>
)