package com.project.hello.vehicle.domain.steps

import com.project.hello.city.plan.domain.model.Line

interface OutputAnalysis {
    fun mostProbableLine(linesToAnalyse: List<LineWithAccuracy>): Line?
}