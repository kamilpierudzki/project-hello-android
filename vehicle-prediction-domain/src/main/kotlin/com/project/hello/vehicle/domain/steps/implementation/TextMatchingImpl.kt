package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.TextMatching
import com.project.hello.vehicle.domain.steps.UniversalTransformation

class TextMatchingImpl(
    private val universalTransformation: UniversalTransformation
) : TextMatching {

    override fun isNumberMatching(input: String, cityLine: Line): Boolean =
        universalTransformation.transformedText(cityLine.number) ==
                universalTransformation.transformedText(input)

    override fun isNumberSliceMatching(input: String, cityLine: Line): Boolean =
        isNumberContainingInput(input, cityLine) ||
                isInputContainingNumber(input, cityLine)

    override fun isDestinationMatching(input: String, cityLine: Line): Boolean =
        universalTransformation.transformedText(cityLine.destination) ==
                universalTransformation.transformedText(input)

    override fun isDestinationSliceMatching(input: String, cityLine: Line): Boolean =
        isDestinationContainingInput(input, cityLine) ||
                isInputContainingDestination(input, cityLine)

    private fun isNumberContainingInput(input: String, cityLine: Line): Boolean =
        universalTransformation.transformedText(cityLine.number)
            .contains(universalTransformation.transformedText(input))

    private fun isInputContainingNumber(input: String, cityLine: Line): Boolean =
        universalTransformation.transformedText(input)
            .contains(universalTransformation.transformedText(cityLine.number))

    private fun isDestinationContainingInput(input: String, cityLine: Line): Boolean =
        universalTransformation.transformedText(cityLine.destination)
            .contains(universalTransformation.transformedText(input))

    private fun isInputContainingDestination(input: String, cityLine: Line): Boolean =
        universalTransformation.transformedText(input)
            .contains(universalTransformation.transformedText(cityLine.destination))
}