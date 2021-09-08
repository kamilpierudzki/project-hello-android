package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.TextMatching
import com.project.hello.vehicle.domain.steps.TextMatchingResult
import com.project.hello.vehicle.domain.steps.UniversalTransformation

class TextMatchingImpl(
    private val universalTransformation: UniversalTransformation
) : TextMatching {

    override fun isNumberMatching(input: String, cityLine: Line): TextMatchingResult {
        val transformedNumber = universalTransformation.transformedText(cityLine.number)
        val transformedInput = universalTransformation.transformedText(input)
        val isMatching = transformedNumber == transformedInput
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
        val transformedInput = universalTransformation.transformedText(input)
        val isMatching = transformedDestination == transformedInput
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
        val transformedInput = universalTransformation.transformedText(input)
        val isContaining = transformedNumber.contains(transformedInput)
        return if (isContaining && input.isNotBlank()) {
            val percentage = calculatePercentage(transformedNumber, transformedInput)
            TextMatchingResult.Positive(percentage)
        } else {
            TextMatchingResult.Negative
        }
    }

    private fun isInputContainingNumber(input: String, cityLine: Line): TextMatchingResult {
        val transformedInput = universalTransformation.transformedText(input)
        val transformedNumber = universalTransformation.transformedText(cityLine.number)
        val isContaining = transformedInput.contains(transformedNumber)
        return if (isContaining && input.isNotBlank()) {
            val percentage = calculatePercentage(transformedInput, transformedNumber)
            TextMatchingResult.Positive(percentage)
        } else {
            TextMatchingResult.Negative
        }
    }

    private fun calculatePercentage(leftContains: String, rightContains: String): Int =
        (rightContains.length * 100) / leftContains.length

    private fun isDestinationContainingInput(input: String, cityLine: Line): TextMatchingResult {
        val transformedDestination = universalTransformation.transformedText(cityLine.destination)
        val transformedInput = universalTransformation.transformedText(input)
        val isContaining = transformedDestination.contains(transformedInput)
        return if (isContaining && input.isNotBlank()) {
            val percentage = calculatePercentage(transformedDestination, transformedInput)
            TextMatchingResult.Positive(percentage)
        } else {
            TextMatchingResult.Negative
        }
    }

    private fun isInputContainingDestination(input: String, cityLine: Line): TextMatchingResult {
        val transformedInput = universalTransformation.transformedText(input)
        val transformedDestination = universalTransformation.transformedText(cityLine.destination)
        val isContaining = transformedInput.contains(transformedDestination)
        return if (isContaining && input.isNotBlank()) {
            val percentage = calculatePercentage(transformedInput, transformedDestination)
            TextMatchingResult.Positive(percentage)
        } else {
            TextMatchingResult.Negative
        }
    }
}