package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.vehicle.domain.steps.Reduction

private const val MIN_SIZE_OF_INPUT_TEXT = 4

class ReductionImpl : Reduction {

    override fun reducedInputs(
        inputs: List<String>,
        textsFromInputUsedToMatch: List<String>,
        numbersNotMatched: MutableList<String>
    ): List<String> {
        /*val reducedInputs1 = transformedInput
            .filter {
                textsFromInputUsedToMatch.contains(it).not()
            }*/

        val reducedInputs2 = /*reducedInputs1*/inputs.filter {
            if (containsNumber(it)) {
                if (allCharactersAreDigits(it)) {
                    if (!numbersNotMatched.contains(it)) {
                        numbersNotMatched.add(it)
                        true
                    } else {
                        false
                    }
                } else {
                    true
                }
            } else {
                it.length >= MIN_SIZE_OF_INPUT_TEXT
            }
        }

        return reducedInputs2
    }

    private fun allCharactersAreDigits(input: String): Boolean = input.toIntOrNull() != null

    private fun containsNumber(input: String): Boolean =
        input.firstOrNull { character ->
            character.toString().toIntOrNull() != null
        } != null
}