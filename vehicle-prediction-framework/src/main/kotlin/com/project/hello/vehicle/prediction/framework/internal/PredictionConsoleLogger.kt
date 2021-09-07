package com.project.hello.vehicle.prediction.framework.internal

import com.project.hello.vehicle.domain.analysis.LineWithProbability

interface PredictionConsoleLogger {
    fun logPredictedLines(predictions: List<LineWithProbability>)
    fun logAnalysedResolution(width: Int, height: Int)
    fun logRawRecognisedTexts(texts: List<String>)
}