package com.project.hallo.vehicle.domain.steps

interface Reduction {
    fun reduceInput(
        inputs: List<String>,
        textsUsedInMatch: List<String> = emptyList(),
        numbersNotMatched: MutableList<String> = mutableListOf()
    ): List<String>
}