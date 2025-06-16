package com.badmanners.murglar.lib.core.utils.fuzzywuzzy

import me.xdrop.fuzzywuzzy.FuzzySearch
import me.xdrop.fuzzywuzzy.ToStringFunction
import me.xdrop.fuzzywuzzy.algorithms.BasicAlgorithm
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


class NonSetWeightedRatio : BasicAlgorithm() {

    companion object {
        const val UNBASE_SCALE = .95
        const val PARTIAL_SCALE = .90
        const val TRY_PARTIALS = true
    }

    override fun apply(arg1: String, arg2: String, stringProcessor: ToStringFunction<String>): Int {
        val s1 = stringProcessor.apply(arg1)
        val s2 = stringProcessor.apply(arg2)

        val len1 = s1.length
        val len2 = s2.length

        if (len1 == 0 || len2 == 0)
            return 0

        var tryPartials = TRY_PARTIALS
        val unbaseScale = UNBASE_SCALE
        var partialScale = PARTIAL_SCALE

        val base = FuzzySearch.ratio(s1, s2)
        val lenRatio = max(len1, len2).toDouble() / min(len1, len2)

        // if strings are similar length don't use partials
        if (lenRatio < 1.5)
            tryPartials = false

        // if one string is much shorter than the other
        if (lenRatio > 8)
            partialScale = .6

        return if (tryPartials) {
            val partial = FuzzySearch.partialRatio(s1, s2) * partialScale
            val partialSor = FuzzySearch.tokenSortPartialRatio(s1, s2) * unbaseScale * partialScale
            max(max(base.toDouble(), partial), partialSor).roundToInt()
        } else {
            val tokenSort = FuzzySearch.tokenSortRatio(s1, s2) * unbaseScale
            max(base.toDouble(), tokenSort).roundToInt()
        }
    }
}