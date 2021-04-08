package com.project.halo.vehicle.domain.steps

interface Fragmentation {
    fun fragmentedInput(inputs: List<String>): List<String>
}