package com.project.hello.vehicle.prediction.steps.implementation

import com.project.hello.transit.agency.model.Line
import com.project.hello.vehicle.prediction.VehiclePrediction
import com.project.hello.vehicle.prediction.steps.*
import com.project.hello.vehicle.prediction.timeout.TimeoutChecker

class VehiclePredictionImpl(
    private val matchingCityLines: MatchingCityLines,
    private val reduction: Reduction,
    private val fragmentation: Fragmentation,
    private val outputAnalysis: OutputAnalysis,
    private val universalTransformation: UniversalTransformation
) : VehiclePrediction {

    override fun predictLine(
        rawInput: String,
        cityLines: List<Line>,
        timeoutChecker: TimeoutChecker
    ): Line? {
        val transformedInput = universalTransformation.transformedText(rawInput)
        val fragmentedInput = fragmentation.fragmentedInput(transformedInput)
        val reducedFragmentedInput = reduction.reducedInput(fragmentedInput)
        val linesMatchedBasedOnInput = matchingCityLines.cityLinesMatchedBasedOnInput(
            reducedFragmentedInput,
            cityLines,
            timeoutChecker
        )
        return outputAnalysis.mostProbableLine(linesMatchedBasedOnInput)
    }
}