package com.project.hello.vehicle.prediction.framework.internal.logger

import com.project.hello.transit.agency.domain.model.Line
import com.project.hello.vehicle.domain.analysis.LineWithShare

interface PredictionConsoleLogger {
    fun logPredictedLine(predicted: Line?)
    fun logBufferedLine(buffered: LineWithShare?)
    fun logAnalysedResolution(width: Int, height: Int)
    fun logRawRecognisedText(recognisedText: String)
}