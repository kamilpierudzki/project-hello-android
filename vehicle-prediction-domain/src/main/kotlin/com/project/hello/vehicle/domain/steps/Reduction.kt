package com.project.hello.vehicle.domain.steps

interface Reduction {
    fun reduceInput(
        inputs: List<String>,
        textsUsedInMatch: List<String> = emptyList(),
        numbersNotMatched: MutableList<String> = mutableListOf()
    ): List<String>
}