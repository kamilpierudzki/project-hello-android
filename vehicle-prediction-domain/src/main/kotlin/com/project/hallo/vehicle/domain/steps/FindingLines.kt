package com.project.hallo.vehicle.domain.steps

import com.project.hallo.city.plan.domain.model.Line

interface FindingLines {
    fun foundLinesData(inputs: List<String>, cityLines: List<Line>): FoundData
}

data class FoundData(
    val matchedLines: MutableList<LineWithAccuracy>,
    val textsUsedInMatch: List<String>
)

