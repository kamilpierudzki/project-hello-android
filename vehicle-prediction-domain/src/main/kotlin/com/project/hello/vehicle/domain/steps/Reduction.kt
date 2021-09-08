package com.project.hello.vehicle.domain.steps

interface Reduction {
    fun reducedInput(input: List<String>): List<String>
}