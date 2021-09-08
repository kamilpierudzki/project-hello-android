package com.project.hello.vehicle.prediction.framework.internal

import com.project.hello.vehicle.domain.analysis.LineWithProbability

sealed class PredictedLineEvent {
    data class Positive(val lineWithProbability: LineWithProbability) : PredictedLineEvent()
    object Negative : PredictedLineEvent()
}
