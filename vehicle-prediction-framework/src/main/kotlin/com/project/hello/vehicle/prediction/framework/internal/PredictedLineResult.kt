package com.project.hello.vehicle.prediction.framework.internal

import com.project.hello.transit.agency.domain.model.Line

sealed class PredictedLineResult {
    data class Positive(val line: Line) : PredictedLineResult()
    object ConfidenceTooLow : PredictedLineResult()
    object Negative : PredictedLineResult()
}
