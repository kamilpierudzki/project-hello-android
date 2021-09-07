package com.project.hello.vehicle.prediction.framework.internal.implementation

import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.prediction.framework.internal.PredictionConsoleLogger
import javax.inject.Inject

internal class PredictionConsoleLoggerImpl @Inject constructor() : PredictionConsoleLogger {

    override fun logPredictedLines(predictions: List<LineWithProbability>) {
        android.util.Log.d(
            "test_predicted",
            "predicted: \n${predictions.map { p -> "\n$p" }}\n"
        )
    }

    override fun logAnalysedResolution(width: Int, height: Int) {
        android.util.Log.d("test_resolution", "width: $width, height: $height")
    }

    override fun logRawRecognisedTexts(texts: List<String>) {
        android.util.Log.d("test_raw", "raw texts: $texts")
    }
}