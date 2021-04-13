package com.project.hallo.vehicle.domain.steps

import com.project.hallo.city.plan.domain.Line

data class LineWithAccuracy(val line: Line, val accuracyLevel: AccuracyLevel) {

    val anyMatched = accuracyLevel != AccuracyLevel.NOT_MATCHED
}