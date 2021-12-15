package com.project.hello.vehicle.prediction.steps

import com.project.hello.transit.agency.model.Line
import com.project.hello.vehicle.prediction.timeout.TimeoutChecker

interface MatchingCityLines {

    fun cityLinesMatchedBasedOnInput(
        input: List<String>,
        cityLines: List<Line>,
        timeoutChecker: TimeoutChecker
    ): List<LineWithAccuracy>
}

data class LineWithAccuracy(
    val debugInputChecked: String,
    val line: Line,
    val accuracyInfo: AccuracyInfo
) {

    val anyMatched = accuracyInfo.accuracyLevel != AccuracyLevel.NOT_MATCHED

    override fun toString(): String = "$debugInputChecked, $line, $accuracyInfo"
}

data class AccuracyInfo(val accuracyLevel: AccuracyLevel, val percentage: Int)

