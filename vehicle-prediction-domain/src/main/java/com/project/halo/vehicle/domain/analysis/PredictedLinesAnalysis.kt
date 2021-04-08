package com.project.halo.vehicle.domain.analysis

import com.project.hallo.city.plan.domain.Line

interface PredictedLinesAnalysis {

    fun analysedSortedLines(
        newLines: List<Line>,
        currentTimeInMillis: Long,
        onDataChanged: (List<LineWithProbability>) -> Unit
    )
}

data class LineWithProbability(val line: Line, val probability: Float)