package com.project.halo.vehicle.prediction.data.steps

import com.project.hallo.city.plan.domain.Line

data class LineWithAccuracy(val line: com.project.hallo.city.plan.domain.Line, val accuracyLevel: AccuracyLevel) {

    val anyMatched = accuracyLevel != AccuracyLevel.NOT_MATCHED
}