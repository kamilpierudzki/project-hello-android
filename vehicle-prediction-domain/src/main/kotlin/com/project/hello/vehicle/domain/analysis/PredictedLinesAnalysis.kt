package com.project.hello.vehicle.domain.analysis

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.LineWithAccuracy

interface PredictedLinesAnalysis {

    fun analysedSortedLines(
        newLines: List<LineWithAccuracy>,
        currentTimeInMillis: Long
    ): List<LineWithProbability>
}

data class LineWithProbability(
    val line: Line,
    val probability: Float
)