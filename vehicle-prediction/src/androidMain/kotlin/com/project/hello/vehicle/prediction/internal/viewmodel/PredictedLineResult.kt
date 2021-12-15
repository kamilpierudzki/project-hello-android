package com.project.hello.vehicle.prediction.internal.viewmodel

import com.project.hello.transit.agency.model.Line

sealed class PredictedLineResult {
    data class Positive(val line: Line) : PredictedLineResult()
    object ConfidenceTooLow : PredictedLineResult()
    object Negative : PredictedLineResult()
}
