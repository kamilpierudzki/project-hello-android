package com.project.hello.vehicle.prediction.steps

interface Reduction {
    fun reducedInput(input: List<String>): List<String>
}