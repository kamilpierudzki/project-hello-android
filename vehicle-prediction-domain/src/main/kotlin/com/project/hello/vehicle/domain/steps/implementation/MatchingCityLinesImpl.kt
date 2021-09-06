package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.commons.domain.addElementIfNotContains
import com.project.hello.vehicle.domain.steps.*

class MatchingCityLinesImpl(
    private val textMatching: TextMatching
) : MatchingCityLines {

    override fun matchingLinesInfo(input: List<String>, cityLines: List<Line>): MatchingInfo {
        val inputsForWhichThereIsAnyMatch = mutableListOf<String>()
        val foundMatrix: List<List<LineWithAccuracy>> = input
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { input ->
                cityLines
                    .map { cityLine ->
                        transformedInput(input, cityLine, inputsForWhichThereIsAnyMatch)
                    }
                    .filter { lineWithExtra -> lineWithExtra.anyMatched }
            }
            .toList()

        val found: MutableList<LineWithAccuracy> = mutableListOf()
        for (row: List<LineWithAccuracy> in foundMatrix) {
            for (item: LineWithAccuracy in row) {
                found.addElementIfNotContains(item)
            }
        }

        return MatchingInfo(
            linesMatchedBasedOnInput = found,
            textsFromInputUsedToMatch = inputsForWhichThereIsAnyMatch
        )
    }

    private fun transformedInput(
        input: String,
        cityLine: Line,
        inputsForWhichThereIsAnyMatch: MutableList<String>
    ): LineWithAccuracy {
        val accuracy = when {
            textMatching.isNumberMatching(input, cityLine) -> AccuracyLevel.NUMBER_MATCHED
            textMatching.isNumberSliceMatching(input, cityLine) -> AccuracyLevel.NUMBER_SLICE
            textMatching.isDestinationMatching(input, cityLine) -> AccuracyLevel.DESTINATION_MATCHED
            textMatching.isDestinationSliceMatching(input, cityLine) ->
                AccuracyLevel.DESTINATION_SLICE
            else -> AccuracyLevel.NOT_MATCHED
        }

        val lineWithBonus = LineWithAccuracy(cityLine, accuracy)
        if (lineWithBonus.anyMatched) {
            inputsForWhichThereIsAnyMatch.addElementIfNotContains(input)
        }
        return lineWithBonus
    }
}