package com.project.hallo.vehicle.domain.steps

interface OutputAnalysis {
    fun analyseOutput(outputs: List<List<LineWithAccuracy>>): List<com.project.hallo.city.plan.domain.Line>
}