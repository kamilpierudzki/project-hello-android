package com.project.hello.vehicle.domain.steps

interface Reduction {

    fun reducedInputs(
        input: List<String>,
        numbersNotMatched: MutableList<String> = mutableListOf()
    ): List<String>
}