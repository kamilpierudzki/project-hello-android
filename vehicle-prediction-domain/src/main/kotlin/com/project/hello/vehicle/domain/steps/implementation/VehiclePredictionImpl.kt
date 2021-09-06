package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.steps.*

class VehiclePredictionImpl(
    private val matchingCityLines: MatchingCityLines,
    private val reduction: Reduction,
    private val fragmentation: Fragmentation,
    private val outputAnalysis: OutputAnalysis,
    private val universalTransformation: UniversalTransformation
) : VehiclePrediction {

    override fun processInput(rawInput: String, cityLines: List<Line>): List<LineWithAccuracy> {
        val transformedRawInput = universalTransformation.transformedText(rawInput)
        val transformedInput = listOf(transformedRawInput)
        val outputs = matchedLines(transformedInput, cityLines)
        return outputAnalysis.analysedOutputMatrix(outputs)
    }

    private fun matchedLines(
        input: List<String>,
        cityLines: List<Line>,
        numbersNotMatched: MutableList<String> = mutableListOf()
    ): MutableList<MutableList<LineWithAccuracy>> {
        val matchingInfo = matchingCityLines.matchingLinesInfo(input, cityLines)
        val mutableOutputMatrix = mutableListOf(matchingInfo.linesMatchedBasedOnInput)
        val fragmentedInput = fragmentation.fragmentedInput(input)
        val reducedFragmentedInput = reduction.reducedInputs(
            fragmentedInput,
            emptyList(),
            numbersNotMatched
        )
        if (reducedFragmentedInput.isNotEmpty()) {
            val matchedLines = matchedLines(reducedFragmentedInput, cityLines, numbersNotMatched)
            for (matchedLine in matchedLines) {
                mutableOutputMatrix.add(matchedLine)
            }
        }

        return mutableOutputMatrix
    }
}