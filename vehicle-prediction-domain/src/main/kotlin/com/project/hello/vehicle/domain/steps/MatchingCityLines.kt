package com.project.hello.vehicle.domain.steps

import com.project.hello.city.plan.domain.model.Line

interface MatchingCityLines {

    fun cityLinesMatchedBasedOnInput(
        input: List<String>,
        cityLines: List<Line>
    ): List<LineWithAccuracy>
}

data class LineWithAccuracy(
    val inputChecked: String,
    val line: Line,
    val accuracyInfo: AccuracyInfo
) {

    val anyMatched = accuracyInfo.accuracyLevel != AccuracyLevel.NOT_MATCHED

    override fun toString(): String = "$inputChecked, $line, $accuracyInfo"
}

data class AccuracyInfo(val accuracyLevel: AccuracyLevel, val percentage: Int)

