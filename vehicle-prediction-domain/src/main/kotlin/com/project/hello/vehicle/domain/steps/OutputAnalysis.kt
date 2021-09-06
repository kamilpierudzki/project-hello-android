package com.project.hello.vehicle.domain.steps

interface OutputAnalysis {
    fun analysedOutputMatrix(outputMatrix: List<List<LineWithAccuracy>>): List<LineWithAccuracy>
}