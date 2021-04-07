package com.project.halo.vehicle.prediction.data.steps

interface Fragmentation {
    fun fragmentedInput(inputs: List<String>): List<String>
}