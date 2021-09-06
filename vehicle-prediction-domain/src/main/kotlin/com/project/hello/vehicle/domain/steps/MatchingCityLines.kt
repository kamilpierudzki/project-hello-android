package com.project.hello.vehicle.domain.steps

import com.project.hello.city.plan.domain.model.Line

interface MatchingCityLines {
    fun matchingLinesInfo(input: List<String>, cityLines: List<Line>): MatchingInfo
}

data class MatchingInfo(
    val linesMatchedBasedOnInput: MutableList<LineWithAccuracy>,
    val textsFromInputUsedToMatch: List<String>
)

