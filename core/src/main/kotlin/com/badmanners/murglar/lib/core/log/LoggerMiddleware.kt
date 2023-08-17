package com.badmanners.murglar.lib.core.log

import com.badmanners.murglar.lib.core.service.Murglar


/**
 * Logging middleware. Used in [Murglar]s.
 */
interface LoggerMiddleware {

    fun v(tag: String, message: String?)
    fun v(tag: String, tr: Throwable?)
    fun v(tag: String, message: String?, tr: Throwable?)

    fun d(tag: String, message: String?)
    fun d(tag: String, tr: Throwable?)
    fun d(tag: String, message: String?, tr: Throwable?)

    fun i(tag: String, message: String?)
    fun i(tag: String, tr: Throwable?)
    fun i(tag: String, message: String?, tr: Throwable?)

    fun w(tag: String, message: String?)
    fun w(tag: String, tr: Throwable?)
    fun w(tag: String, message: String?, tr: Throwable?)

    fun e(tag: String, message: String?)
    fun e(tag: String, tr: Throwable?)
    fun e(tag: String, message: String?, tr: Throwable?)

    /**
     * Logs to some crash reporting tool.
     */
    fun crashlyticsLog(tag: String, message: String?)

    /**
     * Logs to some crash reporting tool.
     */
    fun crashlyticsLog(tag: String, message: String?, tr: Throwable?)

    /**
     * Logs a very large text to the file/console/etc.
     */
    fun dump(tag: String, largeText: String?)
}