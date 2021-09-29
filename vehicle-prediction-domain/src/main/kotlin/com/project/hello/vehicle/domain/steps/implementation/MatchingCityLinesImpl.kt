package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.commons.domain.addElementIfNotContains
import com.project.hello.vehicle.domain.steps.*

class MatchingCityLinesImpl(
    private val textMatching: TextMatching
) : MatchingCityLines {

    override fun cityLinesMatchedBasedOnInput(
        input: List<String>,
        cityLines: List<Line>,
        timeoutChecker: TimeoutChecker
    ): List<LineWithAccuracy> {
        val inputsForWhichThereIsAnyMatch = mutableListOf<String>()
        return input
            .map { text ->
                cityLines
                    .map { cityLine ->
                        val isTimeout = timeoutChecker.isTimeout()
                        if (isTimeout) {
                            return emptyList()
                        }
                        transformedInput(text, cityLine, inputsForWhichThereIsAnyMatch)
                    }
                    .filter { lineWithAccuracy -> lineWithAccuracy.anyMatched }
            }
            .flatMap { it.toList() }
    }

    private fun transformedInput(
        input: String,
        cityLine: Line,
        inputsForWhichThereIsAnyMatch: MutableList<String>
    ): LineWithAccuracy {
        val numberMatchingResult = textMatching.isNumberMatching(input, cityLine)
        val numberSliceMatchingResult = textMatching.isNumberSliceMatching(input, cityLine)
        val destinationMatchingResult = textMatching.isDestinationMatching(input, cityLine)
        val destinationSliceMatchingResult =
            textMatching.isDestinationSliceMatching(input, cityLine)

        val accuracyInfo = when {
            numberMatchingResult.isPositive ->
                AccuracyInfo(AccuracyLevel.NUMBER_MATCHED, numberMatchingResult.percentage)
            numberSliceMatchingResult.isPositive ->
                AccuracyInfo(AccuracyLevel.NUMBER_SLICE, numberSliceMatchingResult.percentage)
            destinationMatchingResult.isPositive ->
                AccuracyInfo(
                    AccuracyLevel.DESTINATION_MATCHED,
                    destinationMatchingResult.percentage
                )
            destinationSliceMatchingResult.isPositive ->
                AccuracyInfo(
                    AccuracyLevel.DESTINATION_SLICE,
                    destinationSliceMatchingResult.percentage
                )
            else -> AccuracyInfo(AccuracyLevel.NOT_MATCHED, 0)
        }

        val lineWithAccuracy = LineWithAccuracy(input, cityLine, accuracyInfo)
        if (lineWithAccuracy.anyMatched) {
            inputsForWhichThereIsAnyMatch.addElementIfNotContains(input)
        }
        return lineWithAccuracy
    }

    private val TextMatchingResult.percentage: Int
        get() = (this as TextMatchingResult.Positive).percentage
}