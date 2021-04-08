package com.project.halo.vehicle.domain.steps.implementation

import com.project.halo.vehicle.domain.steps.Reduction
import java.util.*

private const val MIN_SIZE_OF_INPUT_TEXT = 4

class ReductionExperimentalImpl : Reduction {

    override fun reduceInput(
        inputs: List<String>,
        textsUsedInMatch: List<String>,
        numbersNotMatched: MutableList<String>
    ): List<String> {
        val mutableInput = inputs
            .map { it.replace(" ", "").toLowerCase(Locale.ROOT) }
            .toMutableList()

        for (inputThatWasFound: String in textsUsedInMatch) {
            mutableInput.remove(inputThatWasFound)
        }

        mutableInput.removeIf { input ->
            if (isNumberInInput(input)) {
                if (allCharactersAreNumber(input)) {
                    if (!numbersNotMatched.contains(input)) {
                        numbersNotMatched.add(input)
                        false
                    } else {
                        true
                    }
                } else {
                    false
                }
            } else {
                input.length < MIN_SIZE_OF_INPUT_TEXT
            }
        }

        return mutableInput
    }

    private fun allCharactersAreNumber(input: String): Boolean = input.toIntOrNull() != null

    private fun isNumberInInput(input: String): Boolean =
        input.firstOrNull { character ->
            character.toString().toIntOrNull() != null
        } != null
}