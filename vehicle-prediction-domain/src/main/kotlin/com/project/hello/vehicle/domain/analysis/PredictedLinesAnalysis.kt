package com.project.hello.vehicle.domain.analysis

import com.project.hello.vehicle.domain.steps.LineWithAccuracy

interface PredictedLinesAnalysis {

    fun analysedSortedLines(
        newLines: List<LineWithAccuracy>,
        currentTimeInMillis: Long
    ): List<LineWithAccuracyAndProbability>
}

data class LineWithAccuracyAndProbability(
    val lineWithAccuracy: LineWithAccuracy,
    val probability: Float
)