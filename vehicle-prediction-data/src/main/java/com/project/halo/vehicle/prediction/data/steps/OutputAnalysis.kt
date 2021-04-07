package com.project.halo.vehicle.prediction.data.steps

import com.project.hallo.city.plan.domain.Line

interface OutputAnalysis {
    fun analyseOutput(outputs: List<List<LineWithAccuracy>>): List<com.project.hallo.city.plan.domain.Line>
}