package com.project.hello.vehicle.prediction.internal.logger

import com.project.hello.transit.agency.model.Line
import com.project.hello.vehicle.prediction.analysis.LineWithShare

interface PredictionConsoleLogger {
    fun logPredictedLine(predicted: Line?)
    fun logBufferedLine(buffered: LineWithShare?)
    fun logAnalysedResolution(width: Int, height: Int)
    fun logRawRecognisedText(recognisedText: String)
    fun logUsedCityLines(cityLines: List<Line>)
    fun cityLinesAreUpdated(size: Int)
}