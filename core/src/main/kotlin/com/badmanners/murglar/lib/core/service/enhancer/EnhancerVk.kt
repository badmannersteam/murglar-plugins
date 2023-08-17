package com.badmanners.murglar.lib.core.service.enhancer

import com.badmanners.murglar.lib.core.utils.contract.WorkerThread


interface EnhancerVk {

    val canGetTrackUrl: Boolean

    @WorkerThread
    fun getTrackUrl(trackId: String): String
}