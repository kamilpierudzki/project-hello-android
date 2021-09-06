package com.project.hello.vehicle.domain.steps

interface Reduction {
    fun reducedInputs(
        inputs: List<String>,
        textsFromInputUsedToMatch: List<String> = emptyList(),
        numbersNotMatched: MutableList<String> = mutableListOf()
    ): List<String>
}