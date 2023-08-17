package com.badmanners.murglar.lib.core.utils.pattern

import java.util.regex.Matcher.quoteReplacement
import java.util.regex.Pattern


/**
 * Pattern matcher.
 *
 * https://github.com/freewind/UrlMatcher
 *
 * @see match
 * @see matchFromCollection
 * @see matchAllFromCollection
 * @see main
 */
object PatternMatcher {

    private val NAME_PATTERN = Pattern.compile("<([^()]+?)>")

    data class PatternMatchResult<T>(
        val pattern: String,
        val parameters: Map<String, String>,
        val value: T
    )

    /**
     * Finds first value from collection, which pattern matches with input.
     */
    fun <V, T> matchFromCollection(
        input: String,
        collection: Collection<T>,
        patternSelector: (T) -> String,
        valueSelector: (T) -> V
    ): PatternMatchResult<V>? {
        for (item in collection) {
            val pattern = patternSelector(item)
            val parameters = match(pattern, input)
            if (parameters != null)
                return PatternMatchResult(pattern, parameters, valueSelector(item))
        }
        return null
    }

    /**
     * Finds all values from collection, which patterns match with input.
     */
    fun <V, T> matchAllFromCollection(
        input: String,
        collection: Collection<T>,
        patternSelector: (T) -> String,
        valueSelector: (T) -> V
    ): List<PatternMatchResult<V>> = collection.mapNotNull { item ->
        val pattern = patternSelector(item)
        match(pattern, input)?.let { parameters ->
            PatternMatchResult(pattern, parameters, valueSelector(item))
        }
    }

    /**
     * Checks that passed input matches the passed pattern.
     *
     * For supported patterns see [PatternMatcher.main].
     * Round brackets - "(" and ")" - aren't supported in parameter names or regexes.
     *
     * @return Map [parameter from pattern -> matched value] or null, if input doesn't match the pattern.
     */
    fun match(pattern: String, input: String): Map<String, String>? {
        if (!pattern.contains('<') && pattern == input)
            return emptyMap()

        val regexPattern = toRegex(pattern)
        val names = getNames(pattern)

        val matcher = Pattern.compile(regexPattern).matcher(input)
        if (!matcher.matches())
            return null

        return LinkedHashMap<String, String>().apply {
            for (i in 0 until matcher.groupCount())
                this[names[i]] = matcher.group(i + 1)
        }
    }

    private fun toRegex(pattern: String): String {
        var regex = "\\Q" + pattern.replace("<", "\\E<").replace(">", ">\\Q") + "\\E"

        val end = "*\\E"
        if (regex.endsWith(end))
            regex = regex.substring(0, regex.length - end.length) + "\\E.*"

        val start = "\\Q*"
        if (regex.startsWith(start))
            regex = ".*\\Q" + regex.substring(start.length)

        regex = "^" + regex.replace("\\Q\\E", "") + "$"

        val matcher = NAME_PATTERN.matcher(regex)
        val sb = StringBuffer()
        while (matcher.find()) {
            val name = matcher.group(1)
            if (name.contains(":")) {
                val value = name.substringAfter(":")
                matcher.appendReplacement(sb, "(" + quoteReplacement(value) + ")")
            } else
                matcher.appendReplacement(sb, quoteReplacement("([^/]+)"))
        }
        matcher.appendTail(sb)

        return sb.toString()
    }

    private fun getNames(pattern: String): List<String> {
        val names = mutableListOf<String>()
        val matcher = NAME_PATTERN.matcher(pattern)
        while (matcher.find()) {
            var name = matcher.group(1)
            if (name.contains(":"))
                name = name.substringBefore(":")
            names.add(name)
        }
        return names
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // @formatter:off
        // plain match
        var result = match("", "")
        check(result != null && result.isEmpty())
        result = match("/", "/")
        check(result != null && result.isEmpty())
        result = match("/index", "/index")
        check(result != null && result.isEmpty())
        result = match("/index/", "/index/")
        check(result != null && result.isEmpty())

        // item match
        result = match("/users/<name>", "/users/freewind")
        check(result != null && result.size == 1 && result["name"] == "freewind")
        result = match("/users/~<name>", "/users/~freewind")
        check(result != null && result.size == 1 && result["name"] == "freewind")
        result = match("/users/<name>/edit", "/users/freewind/edit")
        check(result != null && result.size == 1 && result["name"] == "freewind")
        result = match("/users/<name>/<action>", "/users/freewind/edit")
        check(result != null && result.size == 2 && result["name"] == "freewind" && result["action"] == "edit")
        result = match("/users/<name>.<suffix>", "/users/detail.json")
        check(result != null && result.size == 2 && result["name"] == "detail" && result["suffix"] == "json")
        result = match("/users/<name>.<suffix>", "/users/detail.")
        check(result == null)

        // tail * match
        result = match("/users/*", "/users/")
        check(result != null && result.isEmpty())
        result = match("/users/*", "/users/freewind")
        check(result != null && result.isEmpty())
        result = match("/users/*", "/users/freewind/edit")
        check(result != null && result.isEmpty())
        result = match("/users/*", "/usersfreewind")
        check(result == null)

        // head * match
        result = match("*/users/<userId>", "server/game/users/freewind")
        check(result != null && result.size == 1 && result["userId"] == "freewind")
        result = match("*/users/<userId>", "server/gameusers/freewind")
        check(result == null)
        result = match("*users/<userId>", "server/game/users/freewind")
        check(result != null && result.size == 1 && result["userId"] == "freewind")
        result = match("*users/<userId>", "users/freewind")
        check(result != null && result.size == 1 && result["userId"] == "freewind")

        // regex match
        result = match("/users/<id:\\d+>", "/users/123456")
        check(result != null && result.size == 1 && result["id"] == "123456")
        result = match("/users/<id:\\d{2}>", "/users/12")
        check(result != null && result.size == 1 && result["id"] == "12")
        result = match("/users/<id:\\d{2}>", "/users/123")
        check(result == null)
        result = match("/users/<id:\\d+>", "/users/123456abc")
        check(result == null)
        result = match("*owner-<ownerId>_playlist-<playlistId>_hash-<hash:\\w*>", "abc/owner--3213123_playlist-244_hash-e5b6c675a408772e1e")
        check(result != null && result.size == 3 && result["ownerId"] == "-3213123" && result["playlistId"] == "244" && result["hash"] == "e5b6c675a408772e1e")
        result = match("*owner-<ownerId>_playlist-<playlistId>_hash-<hash:\\w*>", "abc/owner--3213123_playlist-244_hash-")
        check(result != null && result.size == 3 && result["ownerId"] == "-3213123" && result["playlistId"] == "244" && result["hash"] == "")
        // @formatter:on
    }

    private fun check(test: Boolean) = check(test) { "Assert failed" }
}