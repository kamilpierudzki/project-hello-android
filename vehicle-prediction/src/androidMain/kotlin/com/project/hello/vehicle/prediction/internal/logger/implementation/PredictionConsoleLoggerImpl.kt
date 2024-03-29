package com.project.hello.vehicle.prediction.internal.logger.implementation

import com.project.hello.transit.agency.model.Line
import com.project.hello.vehicle.prediction.analysis.LineWithShare
import com.project.hello.vehicle.prediction.internal.logger.PredictionConsoleLogger
import javax.inject.Inject

internal class PredictionConsoleLoggerImpl @Inject constructor() : PredictionConsoleLogger {

    override fun logBufferedLine(buffered: LineWithShare?) {
        android.util.Log.d("test_buffered", "buffered: $buffered")
    }

    override fun logPredictedLine(predicted: Line?) {
        android.util.Log.d("test_predicted", "predicted: $predicted")
    }

    override fun logAnalysedResolution(width: Int, height: Int) {
        android.util.Log.d("test_resolution", "width: $width, height: $height")
    }

    override fun logRawRecognisedText(recognisedText: String) {
        android.util.Log.d("test_raw", "raw text: $recognisedText")
    }

    override fun logUsedCityLines(cityLines: List<Line>) {
        android.util.Log.d("test_city_lines", "${cityLines.map { "$it\n" }}")
    }

    override fun cityLinesAreUpdated(size: Int) {
        android.util.Log.d(
            "test_city_lines_updated",
            "---------CITY LINES UPDATED-------- $size --------------"
        )
    }
}