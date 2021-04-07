package com.project.halo.vehicle.prediction.data.steps

import com.project.hallo.city.plan.domain.Line

interface FindingLines {
    fun foundLinesData(inputs: List<String>, cityLines: List<Line>): FoundData
}

data class FoundData(
    val matchedLines: MutableList<LineWithAccuracy>,
    val textsUsedInMatch: List<String>
)

