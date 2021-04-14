package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.vehicle.domain.steps.CountryCharactersProvider
import com.project.hallo.vehicle.domain.steps.TextMatching

class TextMatchingImpl(
    private val countryCharactersProvider: CountryCharactersProvider
) : TextMatching {

    override fun didNumberMatch(input: String, cityLine: Line): Boolean =
        transformedText(cityLine.number) == transformedText(input)

    override fun didDestinationMatch(input: String, cityLine: Line): Boolean =
        transformedText(cityLine.destination) == transformedText(input)

    override fun didNumberContains(input: String, cityLine: Line): Boolean =
        transformedText(cityLine.number).contains(transformedText(input))

    override fun didDestinationContain(input: String, cityLine: Line): Boolean =
        transformedText(cityLine.destination).contains(transformedText(input))

    override fun didSliceMatch(input: String, cityLine: Line): Boolean =
        didNumberContains(input, cityLine) || didDestinationContain(input, cityLine)

    private fun transformedText(input: String): String =
        input
            .replace(" ", "", ignoreCase = true)
            .toLowerCase()
            .map { c ->
                val stringCharacter = c.toString()
                countryCharactersProvider.get().getOrDefault(stringCharacter, stringCharacter)
            }
            .reduce { acc: String, s: String -> acc + s }
}