package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.vehicle.domain.steps.CountryCharactersProvider
import com.project.hello.vehicle.domain.steps.UniversalTransformation

class UniversalTransformationImpl(
    private val countryCharactersProvider: CountryCharactersProvider
) : UniversalTransformation {

    override fun transformedText(text: String): String =
        if (text.isBlank()) {
            text
        } else {
            text
                .replace(" ", "", ignoreCase = true)
                .lowercase()
                .map { c ->
                    val stringCharacter = c.toString()
                    countryCharactersProvider.get().getOrElse(stringCharacter, { stringCharacter })
                }
                .reduce { acc: String, s: String -> acc + s }
        }
}
