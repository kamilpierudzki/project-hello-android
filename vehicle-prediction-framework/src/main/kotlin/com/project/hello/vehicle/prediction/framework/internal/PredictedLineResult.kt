package com.project.hello.vehicle.prediction.framework.internal

import com.project.hello.vehicle.domain.analysis.LineWithProbability

sealed class PredictedLineResult {
    data class Positive(val lineWithProbability: LineWithProbability) : PredictedLineResult()
    object Negative : PredictedLineResult()
}
