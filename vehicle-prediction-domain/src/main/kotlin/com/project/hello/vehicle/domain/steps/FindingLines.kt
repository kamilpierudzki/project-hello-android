package com.project.hello.vehicle.domain.steps

import com.project.hello.city.plan.domain.model.Line

interface FindingLines {
    fun foundLinesData(inputs: List<String>, cityLines: List<Line>): FoundData
}

data class FoundData(
    val matchedLines: MutableList<LineWithAccuracy>,
    val textsUsedInMatch: List<String>
)

