package com.project.hallo.vehicle.domain.analysis

import com.project.hallo.city.plan.domain.Line

interface PredictedLinesAnalysis {

    fun analysedSortedLines(
        newLines: List<Line>,
        currentTimeInMillis: Long
    ): List<LineWithProbability>
}

data class LineWithProbability(val line: Line, val probability: Float)