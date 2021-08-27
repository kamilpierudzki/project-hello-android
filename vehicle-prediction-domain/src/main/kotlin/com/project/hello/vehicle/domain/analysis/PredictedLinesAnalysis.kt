package com.project.hello.vehicle.domain.analysis

import com.project.hello.city.plan.domain.model.Line

interface PredictedLinesAnalysis {

    fun analysedSortedLines(
        newLines: List<Line>,
        currentTimeInMillis: Long
    ): List<LineWithProbability>
}

data class LineWithProbability(val line: Line, val probability: Float)