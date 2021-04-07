package com.project.halo.vehicle.domain.steps

import com.project.halo.vehicle.prediction.data.steps.Reduction
import java.util.*

private const val MIN_SIZE_OF_INPUT_ELEMENT = 3

class ReductionImpl : Reduction {

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

        mutableInput.removeIf { it.length <= MIN_SIZE_OF_INPUT_ELEMENT }

        return mutableInput
    }
}