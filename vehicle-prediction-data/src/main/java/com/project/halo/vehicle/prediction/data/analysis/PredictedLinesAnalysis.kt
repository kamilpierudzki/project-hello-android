package com.project.halo.vehicle.prediction.data.analysis

import com.project.hallo.city.plan.domain.Line

interface PredictedLinesAnalysis {
    fun analysedLines(
        newLines: List<Line>,
        currentTimeInMillis: Long,
        onDataChanged: (List<LineWithProbability>) -> Unit
    )
}

data class LineWithProbability(val line: Line, val probability: Float)