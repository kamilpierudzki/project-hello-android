package com.project.hello.vehicle.prediction.steps

import com.project.hello.transit.agency.model.Line

interface OutputAnalysis {
    fun mostProbableLine(linesToAnalyse: List<LineWithAccuracy>): Line?
}