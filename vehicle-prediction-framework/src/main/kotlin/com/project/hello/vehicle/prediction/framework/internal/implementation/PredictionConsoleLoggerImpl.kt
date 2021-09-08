package com.project.hello.vehicle.prediction.framework.internal.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.prediction.framework.internal.PredictionConsoleLogger
import javax.inject.Inject

internal class PredictionConsoleLoggerImpl @Inject constructor() : PredictionConsoleLogger {

    override fun logBufferedLine(buffered: LineWithProbability?) {
        android.util.Log.d(
            "test_buffered",
            "buffered: $buffered"
        )
    }

    override fun logPredictedLine(predicted: Line?) {
        android.util.Log.d(
            "test_predicted",
            "predicted: $predicted"
        )
    }

    override fun logAnalysedResolution(width: Int, height: Int) {
        android.util.Log.d("test_resolution", "width: $width, height: $height")
    }

    override fun logRawRecognisedTexts(texts: List<String>) {
        android.util.Log.d("test_raw", "raw texts: $texts")
    }
}