package com.badmanners.murglar.lib.core.utils

import com.badmanners.murglar.lib.core.utils.MurglarLibUtils.isAndroid


object UserAgent {

    private const val DEFAULT_CHROME_DESKTOP_DEVICE_DATA = "Windows NT 10.0; Win64; x64"

    private const val DEFAULT_CHROME_MOBILE_DEVICE_DATA = "Linux; Android 13; Pixel 8"

    @JvmField
    val CHROME_DESKTOP_USER_AGENT = "Mozilla/5.0 (${chromeDesktop()}) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/147.0.0.0 Safari/537.36"

    @JvmField
    val CHROME_MOBILE_USER_AGENT = "Mozilla/5.0 (${chromeMobile()}) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/147.0.7727.138 Mobile Safari/537.36"

    private fun chromeDesktop(): String = when {
        isAndroid() -> DEFAULT_CHROME_DESKTOP_DEVICE_DATA
        else -> {
            val osName = System.getProperty("os.name").orEmpty()
            when {
                osName.contains("Linux", ignoreCase = true) -> "X11; Linux x86_64"
                osName.startsWith("Mac", ignoreCase = true) -> "Macintosh; Intel Mac OS X 10_15_7"
                else -> DEFAULT_CHROME_DESKTOP_DEVICE_DATA
            }
        }
    }

    private fun chromeMobile(): String = when {
        !isAndroid() -> DEFAULT_CHROME_MOBILE_DEVICE_DATA
        else -> {
            val release = getAndroidStringField("android.os.Build\$VERSION", "RELEASE")
                ?: DEFAULT_CHROME_MOBILE_DEVICE_DATA.substringAfter("Android ").substringBefore(';')
            val model = getAndroidStringField("android.os.Build", "MODEL")
                ?: DEFAULT_CHROME_MOBILE_DEVICE_DATA.substringAfterLast("; ")
            "Linux; Android $release; $model"
        }
    }

    private fun getAndroidStringField(className: String, fieldName: String) = try {
        Class.forName(className)
            .getDeclaredField(fieldName)
            .get(null)
            ?.toString()
            ?.replace("[;()]".toRegex(), " ")
            ?.replace("\\s+".toRegex(), " ")
            ?.trim()
            ?.takeIf(String::isNotBlank)
    } catch (_: Throwable) {
        null
    }
}