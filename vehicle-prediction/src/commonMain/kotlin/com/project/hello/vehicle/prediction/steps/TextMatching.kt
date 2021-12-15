package com.project.hello.vehicle.prediction.steps

import com.project.hello.transit.agency.model.Line

interface TextMatching {
    fun isNumberMatching(input: String, cityLine: Line): TextMatchingResult
    fun isNumberSliceMatching(input: String, cityLine: Line): TextMatchingResult
    fun isDestinationMatching(input: String, cityLine: Line): TextMatchingResult
    fun isDestinationSliceMatching(input: String, cityLine: Line): TextMatchingResult
}

sealed class TextMatchingResult {
    data class Positive(val percentage: Int) : TextMatchingResult() {

        init {
            if (percentage !in 0..100) {
                throw IllegalArgumentException("percentage value cannot be greater than 100 or less than 0. Current value $percentage")
            }
        }
    }

    object Negative : TextMatchingResult()

    val isPositive: Boolean get() = this is Positive
}

