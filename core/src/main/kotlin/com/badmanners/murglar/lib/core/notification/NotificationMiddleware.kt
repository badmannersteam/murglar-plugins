package com.badmanners.murglar.lib.core.notification

import com.badmanners.murglar.lib.core.service.Murglar


/**
 * Notification middleware.
 *
 * Used in [Murglar]s for user notifying.
 */
interface NotificationMiddleware {

    fun shortNotify(text: String)

    fun longNotify(text: String)
}