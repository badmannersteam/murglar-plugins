package com.badmanners.murglar.lib.core.utils

import org.apache.commons.lang3.StringUtils
import org.apache.commons.text.StringEscapeUtils
import java.net.HttpCookie
import java.net.MalformedURLException
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


object MurglarLibUtils {

    const val CHROME_DESKTOP_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"

    const val CHROME_MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; Android 13; Pixel 8) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.6478.71 Mobile Safari/537.36"

    private val DASHES_NORMALIZER = "[\u1806\u2010\u2011\u2012\u2013\u2014\u2015\u2212\u2043\u02D7\u2796\\-]+".toRegex()

    private val HEX_CHARS = "0123456789abcdef".toCharArray()

    /**
     * Calls the specified function [block] with `this` value as its argument and returns its result
     * if condition is true, or just return `this` if condition is false.
     */
    @OptIn(ExperimentalContracts::class)
    inline fun <T> T.letIf(condition: Boolean, block: (T) -> T): T {
        contract {
            callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        }
        return when {
            condition -> block(this)
            else -> this
        }
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> List<*>.castList(): List<T> = this as List<T>

    /**
     * Normalizes string by unescaping HTML tags, replacing dashes to generic one, removing duplicated space characters
     * and limiting size to 500.
     */
    @JvmStatic
    fun String.normalize(): String {
        var string = this
        string = StringEscapeUtils.unescapeHtml4(string)
        string = DASHES_NORMALIZER.replace(string, "-")
        string = StringUtils.normalizeSpace(string)
        string = string.take(500)
        return string
    }

    @JvmStatic
    fun String.optimizeSearchQuery(): String = lowercase()
        .replace(" *\\([^)]*\\) *".toRegex(), " ")
        .replace(" *\\[[^]]*] *".toRegex(), " ")
        .replace("feat.|ft.".toRegex(), "")
        .replace("prod. by|prod.".toRegex(), "")
        .replace("\\s+".toRegex(), " ")

    @JvmStatic
    fun String.containsCyrillic() = indices.any {
        codePointAt(it) in 'А'.code..'я'.code
    }

    @JvmStatic
    fun String.containsJapanese() = indices.any {
        val codePoint = codePointAt(it)
        sequenceOf(
            '\u3041'.code..'\u3096'.code, //Hiragana
            '\u30A0'.code..'\u30FF'.code, //Katakana (Full Width)
            '\u3400'.code..'\u4DB5'.code, //Kanji
            '\u4E00'.code..'\u9FCB'.code, //Kanji
            '\uF900'.code..'\uFA6A'.code, //Kanji
            '\u2E80'.code..'\u2FD5'.code, //Kanji Radicals
            '\uFF5F'.code..'\uFF9F'.code, //Katakana and Punctuation (Half Width)
            '\u3000'.code..'\u303F'.code  //Japanese Symbols and Punctuation
        ).any { range -> codePoint in range }
    }

    /**
     * Masks part of string with replacement value.
     */
    @JvmStatic
    @JvmOverloads
    fun String.mask(keepFirst: Int, keepLast: Int, replacement: String = "**********"): String {
        val endIndex = length - keepLast
        return when {
            endIndex <= keepFirst -> replacement
            else -> replaceRange(keepFirst, endIndex, replacement)
        }
    }

    @JvmStatic
    fun String.uppercaseFirst() = replaceFirstChar(Char::uppercaseChar)

    /**
     * Removes HTML tags from text, replacing the `<br>` with new line.
     */
    @JvmStatic
    fun String.replaceHtml(): String {
        var string = this
        string = string.replace("<br>", "\n")
        string = string.replace("<[^>]+?>".toRegex(), "")
        return string
    }

    /**
     * Sorts list of objects according to list of ids of these objects.
     */
    @JvmStatic
    fun <T, I> MutableList<T>.sortList(idMapper: (T) -> I, orderedIds: List<I>) {
        val indexMap: MutableMap<I, Int> = HashMap()
        for (i in orderedIds.indices)
            indexMap[orderedIds[i]] = i

        sortWith { left, right ->
            val leftIndex = indexMap[idMapper(left)]
            val rightIndex = indexMap[idMapper(right)]
            when {
                leftIndex == null -> -1
                rightIndex == null -> 1
                else -> Integer.compare(leftIndex, rightIndex)
            }
        }
    }

    /**
     * Joins range of list entries with ','.
     */
    @JvmStatic
    @JvmOverloads
    fun List<String>.prepareEntriesString(start: Int = 0, count: Int = size): String = asSequence()
        .drop(start)
        .take(count)
        .joinToString(",")

    /**
     * Performs paged loading of elements, chunked by page size.
     */
    suspend fun <A, T> List<A>.loadPaged(pageSize: Int, loader: suspend (List<A>) -> List<T>): List<T> =
        chunked(pageSize).flatMap { loader(it) }.toList()

    @JvmStatic
    fun String.urlEncode(): String = URLEncoder.encode(this, "UTF-8")

    @JvmStatic
    fun String.urlDecode(): String = URLDecoder.decode(this, "UTF-8")

    @JvmStatic
    fun String.getDecodedQueryParameter(parameterName: String): String? = URL(this).query
        .split("&")
        .asSequence()
        .map { param: String ->
            val nameEnd = param.indexOf("=")
            val name = when {
                nameEnd > 0 -> param.substring(0, nameEnd)
                else -> param
            }
            val value = when {
                nameEnd > 0 && param.length > nameEnd + 1 -> param.substring(nameEnd + 1)
                else -> null
            }
            Pair(name, value)
        }
        .filter { it.first == parameterName && it.second != null }
        .map { it.second!! }
        .map { it.urlDecode() }
        .firstOrNull()

    @JvmStatic
    fun ByteArray.toMD5HexString(): String = MessageDigest.getInstance("MD5").digest(this).toHexString()

    @JvmStatic
    fun ByteArray.toHexString(): String {
        val result = CharArray(size * 2)
        for (i in indices) {
            val v = this[i].toInt() and 0xFF
            result[i * 2] = HEX_CHARS[v ushr 4]
            result[i * 2 + 1] = HEX_CHARS[v and 0x0F]
        }
        return String(result)
    }

    @JvmStatic
    fun String.isHlsUrl(): Boolean = try {
        URL(this).isHlsUrl()
    } catch (_: MalformedURLException) {
        false
    }

    @JvmStatic
    fun URL.isHlsUrl(): Boolean = path.endsWith(".m3u8")

    @JvmStatic
    fun HttpCookie.setHttpOnlySafely(httpOnly: Boolean) {
        try {
            isHttpOnly = httpOnly
        } catch (ignore: Throwable) {
            //on android <api 24 there is no such method
        }
    }

    @JvmStatic
    fun isAndroid() = getAndroidSdkVersion() != null

    @JvmStatic
    fun getAndroidSdkVersion() = try {
        Class.forName("android.os.Build\$VERSION").getDeclaredField("SDK_INT").getInt(null)
    } catch (_: Throwable) {
        null
    }
}