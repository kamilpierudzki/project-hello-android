package com.project.hallo.vehicle.domain.steps

import com.project.hallo.city.plan.domain.model.Line

interface OutputAnalysis {
    fun analyseOutput(outputs: List<List<LineWithAccuracy>>): List<Line>
}