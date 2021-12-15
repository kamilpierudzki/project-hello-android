package com.project.hello.vehicle.prediction.steps

interface Fragmentation {
    fun fragmentedInput(input: String): List<String>
}