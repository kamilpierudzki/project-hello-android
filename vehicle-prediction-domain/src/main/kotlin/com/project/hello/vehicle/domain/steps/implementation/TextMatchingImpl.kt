package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.transit.agency.domain.model.Line
import com.project.hello.vehicle.domain.steps.TextMatching
import com.project.hello.vehicle.domain.steps.TextMatchingResult
import com.project.hello.vehicle.domain.steps.UniversalTransformation

class TextMatchingImpl(
    private val universalTransformation: UniversalTransformation
) : TextMatching {

    override fun isNumberMatching(input: String, cityLine: Line): TextMatchingResult {
        val transformedNumber = universalTransformation.transformedText(cityLine.number)
        val isMatching = transformedNumber.equals(input, ignoreCase = true)
        return if (isMatching && input.isNotBlank()) {
            TextMatchingResult.Positive(100)
        } else {
            TextMatchingResult.Negative
        }
    }

    override fun isNumberSliceMatching(input: String, cityLine: Line): TextMatchingResult {
        val isNumberContainingInputResult = isNumberContainingInput(input, cityLine)
        val isInputContainingNumberResult = isInputContainingNumber(input, cityLine)

        return when {
            isNumberContainingInputResult.isPositive -> isNumberContainingInputResult
            isInputContainingNumberResult.isPositive -> isInputContainingNumberResult
            else -> TextMatchingResult.Negative
        }
    }

    override fun isDestinationMatching(input: String, cityLine: Line): TextMatchingResult {
        val transformedDestination = universalTransformation.transformedText(cityLine.destination)
        val isMatching = transformedDestination.equals(input, ignoreCase = true)
        return if (isMatching && input.isNotBlank()) {
            TextMatchingResult.Positive(100)
        } else {
            TextMatchingResult.Negative
        }
    }

    override fun isDestinationSliceMatching(input: String, cityLine: Line): TextMatchingResult {
        val isDestinationContainingInputResult = isDestinationContainingInput(input, cityLine)
        val isInputContainingDestinationResult = isInputContainingDestination(input, cityLine)

        return when {
            isDestinationContainingInputResult.isPositive -> isDestinationContainingInputResult
            isInputContainingDestinationResult.isPositive -> isInputContainingDestinationResult
            else -> TextMatchingResult.Negative
        }
    }

    private fun isNumberContainingInput(input: String, cityLine: Line): TextMatchingResult {
        val transformedNumber = universalTransformation.transformedText(cityLine.number)
        val isContaining = transformedNumber.contains(input, ignoreCase = true)
        return if (isContaining && input.isNotBlank()) {
            val percentage = calculatePercentage(transformedNumber, input)
            TextMatchingResult.Positive(percentage)
        } else {
            TextMatchingResult.Negative
        }
    }

    private fun isInputContainingNumber(input: String, cityLine: Line): TextMatchingResult {
        val transformedNumber = universalTransformation.transformedText(cityLine.number)
        val isContaining = input.contains(transformedNumber, ignoreCase = true)
        return if (isContaining && input.isNotBlank()) {
            val percentage = calculatePercentage(input, transformedNumber)
            TextMatchingResult.Positive(percentage)
        } else {
            TextMatchingResult.Negative
        }
    }

    private fun calculatePercentage(leftContains: String, rightContains: String): Int =
        (rightContains.length * 100) / leftContains.length

    private fun isDestinationContainingInput(input: String, cityLine: Line): TextMatchingResult {
        val transformedDestination = universalTransformation.transformedText(cityLine.destination)
        val isContaining = transformedDestination.contains(input, ignoreCase = true)
        return if (isContaining && input.isNotBlank()) {
            val percentage = calculatePercentage(transformedDestination, input)
            TextMatchingResult.Positive(percentage)
        } else {
            TextMatchingResult.Negative
        }
    }

    private fun isInputContainingDestination(input: String, cityLine: Line): TextMatchingResult {
        val transformedDestination = universalTransformation.transformedText(cityLine.destination)
        val isContaining = input.contains(transformedDestination, ignoreCase = true)
        return if (isContaining && input.isNotBlank()) {
            val percentage = calculatePercentage(input, transformedDestination)
            TextMatchingResult.Positive(percentage)
        } else {
            TextMatchingResult.Negative
        }
    }
}