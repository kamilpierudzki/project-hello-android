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

    override fun processInput(rawInput: String, cityLines: List<Line>): Line? {
        val transformedInput = universalTransformation.transformedText(rawInput)
        val fragmentedInput = fragmentation.fragmentedInput(transformedInput)
        val reducedFragmentedInput = reduction.reducedInput(fragmentedInput)
        val linesMatchedBasedOnInput = matchingCityLines.cityLinesMatchedBasedOnInput(
            reducedFragmentedInput,
            cityLines
        )
        return outputAnalysis.mostProbableLine(linesMatchedBasedOnInput)
    }
}