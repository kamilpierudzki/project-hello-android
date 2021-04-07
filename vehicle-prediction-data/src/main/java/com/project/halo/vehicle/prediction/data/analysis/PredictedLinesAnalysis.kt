package com.project.halo.vehicle.prediction.data.analysis

import com.project.hallo.city.plan.domain.Line

interface PredictedLinesAnalysis {
    fun analysedLines(
        newLines: List<Line>,
        currentTimeInMillis: Long,
        onDataChanged: (List<Line>) -> Unit
    )
}