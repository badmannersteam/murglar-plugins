package com.badmanners.murglar.lib.core.service.enhancer


interface EnhancerVk {

    val canGetTrackUrl: Boolean

    suspend fun getTrackUrl(trackId: String): String
}