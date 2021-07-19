package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.vehicle.domain.steps.CountryCharactersProvider
import com.project.hallo.vehicle.domain.steps.UniversalTransformation

class UniversalTransformationImpl(
    private val countryCharactersProvider: CountryCharactersProvider
) : UniversalTransformation {

    override fun transformedText(input: String): String =
        if (input.isBlank()) {
            input
        } else {
            input
                .replace(" ", "", ignoreCase = true)
                .toLowerCase()
                .map { c ->
                    val stringCharacter = c.toString()
                    countryCharactersProvider.get().getOrDefault(stringCharacter, stringCharacter)
                }
                .reduce { acc: String, s: String -> acc + s }
        }
}
