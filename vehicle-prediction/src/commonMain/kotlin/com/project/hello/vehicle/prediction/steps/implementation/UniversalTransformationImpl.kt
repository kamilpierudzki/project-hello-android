package com.project.hello.vehicle.prediction.steps.implementation

import com.project.hello.vehicle.prediction.steps.CountryCharactersProvider
import com.project.hello.vehicle.prediction.steps.UniversalTransformation

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
                .asSequence()
                .filter { character -> character.length == 1 }
                .map { stringCharacter -> stringCharacter.toCharArray() }
                .filter { characters -> characters.size == 1 }
                .map { characters -> characters.first() }
                .filter { character ->
                    val code = character.code
                    val isDigit = code in 48..57
                    val isAsciiCharacter = code in 97..122
                    isDigit || isAsciiCharacter
                }
                .map { character -> character.toString() }
                .reduceOrNull { acc: String, s: String -> acc + s }
                .orEmpty()
        }
}
