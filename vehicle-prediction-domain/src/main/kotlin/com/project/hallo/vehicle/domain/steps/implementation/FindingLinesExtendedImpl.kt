package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.city.plan.domain.model.Line
import com.project.hallo.commons.domain.addElementIfNotContains
import com.project.hallo.vehicle.domain.steps.*

class FindingLinesExtendedImpl(
    private val textMatching: TextMatching
) : FindingLines {

    override fun foundLinesData(inputs: List<String>, cityLines: List<Line>): FoundData {
        val specs = mutableListOf<String>()
        val foundMatrix: List<List<LineWithAccuracy>> = inputs
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { input ->
                cityLines
                    .map { cityLine -> transformedInput(input, cityLine, specs) }
                    .filter { lineWithExtra -> lineWithExtra.anyMatched }
            }
            .toList()

        val found: MutableList<LineWithAccuracy> = mutableListOf()
        for (row: List<LineWithAccuracy> in foundMatrix) {
            for (item: LineWithAccuracy in row) {
                found.addElementIfNotContains(item)
            }
        }

        return FoundData(found, specs)
    }

    private fun transformedInput(
        input: String,
        cityLine: Line,
        specs: MutableList<String>
    ): LineWithAccuracy {
        val accuracy = when {
            textMatching.didNumberMatch(input, cityLine) -> AccuracyLevel.NUMBER_MATCHED
            textMatching.didDestinationMatch(input, cityLine) -> AccuracyLevel.DESTINATION_MATCHED
            textMatching.didSliceMatch(input, cityLine) -> AccuracyLevel.SLICE_MATCHED
            else -> AccuracyLevel.NOT_MATCHED
        }

        val lineWithBonus = LineWithAccuracy(cityLine, accuracy)
        if (lineWithBonus.anyMatched) {
            specs.addElementIfNotContains(input)
        }
        return lineWithBonus
    }
}