package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.vehicle.domain.steps.Reduction

private const val MIN_SIZE_OF_INPUT_TEXT = 4

class ReductionImpl : Reduction {

    override fun reducedInput(input: List<String>): List<String> {
        val reducedInput = input.filter {
            containsNumber(it) || it.length >= MIN_SIZE_OF_INPUT_TEXT
        }

        return reducedInput
    }

    private fun containsNumber(input: String): Boolean =
        input.firstOrNull { character ->
            character.toString().toIntOrNull() != null
        } != null
}