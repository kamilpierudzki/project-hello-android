package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.vehicle.domain.steps.CountryCharactersProvider
import com.project.hallo.vehicle.domain.steps.TextMatching
import com.project.hallo.vehicle.domain.steps.UniversalTransformation

class TextMatchingImpl(
    private val universalTransformation: UniversalTransformation
) : TextMatching {

    override fun didNumberMatch(input: String, cityLine: Line): Boolean =
        universalTransformation.transformedText(cityLine.number) ==
                universalTransformation.transformedText(input)

    override fun didDestinationMatch(input: String, cityLine: Line): Boolean =
        universalTransformation.transformedText(cityLine.destination) ==
                universalTransformation.transformedText(input)

    override fun didNumberContains(input: String, cityLine: Line): Boolean =
        universalTransformation.transformedText(cityLine.number)
            .contains(universalTransformation.transformedText(input))

    override fun didDestinationContain(input: String, cityLine: Line): Boolean =
        universalTransformation.transformedText(cityLine.destination)
            .contains(universalTransformation.transformedText(input))

    override fun didSliceMatch(input: String, cityLine: Line): Boolean =
        didNumberContains(input, cityLine) || didDestinationContain(input, cityLine)
}