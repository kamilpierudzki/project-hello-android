package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.vehicle.domain.steps.AccuracyLevel
import com.project.hallo.vehicle.domain.steps.FindingLines
import com.project.hallo.vehicle.domain.steps.FoundData
import com.project.hallo.vehicle.domain.steps.LineWithAccuracy

class FindingLinesImpl : FindingLines {

    override fun foundLinesData(inputs: List<String>, cityLines: List<Line>): FoundData {
        val specs = mutableListOf<String>()
        val foundMatrix: List<List<LineWithAccuracy>> = inputs
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { input ->
                cityLines
                    .map { cityLine ->
                        transformedInput(input, cityLine, specs)
                    }
                    .filter { lineWithExtra ->
                        lineWithExtra.anyMatched
                    }
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
            didNumberMatch(input, cityLine) -> AccuracyLevel.NUMBER_MATCHED
            didDestinationMatch(input, cityLine) -> AccuracyLevel.DESTINATION_MATCHED
            didSliceMatch(input, cityLine) -> AccuracyLevel.SLICE_MATCHED
            else -> AccuracyLevel.NOT_MATCHED
        }

        val lineWithBonus = LineWithAccuracy(cityLine, accuracy)
        if (lineWithBonus.anyMatched) {
            specs.addElementIfNotContains(input)
        }
        return lineWithBonus
    }

    private fun didNumberMatch(input: String, cityLine: Line): Boolean =
        transformedText(cityLine.number) == transformedText(input)

    private fun didDestinationMatch(input: String, cityLine: Line): Boolean {
        return cityLine.destinationVariants.firstOrNull {
            transformedText(it) == transformedText(input)
        } != null
    }

    private fun didNumberContains(input: String, cityLine: Line): Boolean =
        transformedText(cityLine.number).contains(transformedText(input))

    private fun didDestinationContain(input: String, cityLine: Line): Boolean {
        return cityLine.destinationVariants.firstOrNull {
            transformedText(it).contains(transformedText(input))
        } != null
    }

    private fun didSliceMatch(input: String, cityLine: Line): Boolean =
        didNumberContains(input, cityLine) || didDestinationContain(input, cityLine)

    private fun transformedText(input: String): String =
        input
            .replace(" ", "")
            .toLowerCase()
}

internal fun <T> MutableList<T>.addElementIfNotContains(element: T) {
    if (!contains(element)) {
        add(element)
    }
}