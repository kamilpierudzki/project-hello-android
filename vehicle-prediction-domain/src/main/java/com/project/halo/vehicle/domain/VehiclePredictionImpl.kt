package com.project.halo.vehicle.domain

import com.project.hallo.city.plan.domain.Line
import com.project.halo.vehicle.prediction.data.VehiclePrediction
import com.project.halo.vehicle.prediction.data.steps.*

class VehiclePredictionImpl(
    private val findingLines: FindingLines,
    private val reduction: Reduction,
    private val fragmentation: Fragmentation,
    private val outputAnalysis: OutputAnalysis
) : VehiclePrediction {

    override fun processInput(rawInput: String, cityLines: List<Line>): List<Line> {
        val inputs = listOf(rawInput)
        val outputs = matchedLines(inputs, cityLines)
        return outputAnalysis.analyseOutput(outputs)
    }

    private fun matchedLines(
        inputs: List<String>,
        cityLines: List<Line>,
        numbersNotMatched: MutableList<String> = mutableListOf()
    ): MutableList<MutableList<LineWithAccuracy>> {
        val foundLinesData = findingLines.foundLinesData(inputs, cityLines)
        val output: MutableList<MutableList<LineWithAccuracy>> =
            mutableListOf(foundLinesData.matchedLines)

        val reducedInput = reduction.reduceInput(
            inputs,
            foundLinesData.textsUsedInMatch,
            numbersNotMatched
        )
        val fragmentedInput = fragmentation.fragmentedInput(reducedInput)
        val reducedFragmentedInput = reduction.reduceInput(
            fragmentedInput,
            emptyList(),
            numbersNotMatched)
        if (reducedFragmentedInput.isNotEmpty()) {
            val matchedLines = matchedLines(reducedFragmentedInput, cityLines, numbersNotMatched)
            for (matchedLine in matchedLines) {
                output.add(matchedLine)
            }
        }

        return output
    }
}