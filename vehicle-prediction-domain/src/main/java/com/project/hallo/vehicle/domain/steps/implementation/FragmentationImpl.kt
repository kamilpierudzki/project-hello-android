package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.vehicle.domain.steps.Fragmentation
import com.project.hallo.vehicle.domain.steps.UniversalTransformation
import kotlin.math.max

class FragmentationImpl(
    private val universalTransformation: UniversalTransformation
) : Fragmentation {

    override fun fragmentedInput(inputs: List<String>): List<String> {
        return inputs
            .map { universalTransformation.transformedText(it) }
            .map { divideInput(it) }
            .flatMap { it.toList() }
    }

    private fun divideInput(input: String): List<String> {
        val positionToCut = findPositionToCut(input)

        val result = mutableListOf<String>()
        val rangeOfFirstHalf = IntRange(0, positionToCut - 1)
        val firstHalf = input.slice(rangeOfFirstHalf)
        if (firstHalf.isNotEmpty()) {
            result.add(firstHalf)
        }

        val rangeOfSecondHalf = IntRange(positionToCut, input.length - 1)
        val secondHalf = input.slice(rangeOfSecondHalf)
        if (secondHalf.isNotEmpty()) {
            result.add(secondHalf)
        }
        return result
    }

    private fun findPositionToCut(input: String): Int {
        val endPosOfFirstHalf: Int = input.length / 2
        val isLastCharacterOfFirstHalfANumber =
            input[max(endPosOfFirstHalf - 1, 0)].toString().toIntOrNull() != null
        val isFirstCharacterOfSecondHalfANumber =
            input[endPosOfFirstHalf].toString().toIntOrNull() != null
        return if (isLastCharacterOfFirstHalfANumber && isFirstCharacterOfSecondHalfANumber) {
            val isInputANumber = input.toIntOrNull() != null
            if (input.length <= 2 || isInputANumber) {
                input.length
            } else {
                val firstHalfPlusOneCharacterFromSecondHalf =
                    input.slice(IntRange(0, endPosOfFirstHalf))
                findPositionToCut(firstHalfPlusOneCharacterFromSecondHalf)
            }
        } else {
            endPosOfFirstHalf
        }
    }
}