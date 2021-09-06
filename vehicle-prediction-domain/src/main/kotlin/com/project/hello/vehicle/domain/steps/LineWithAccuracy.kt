package com.project.hello.vehicle.domain.steps

import com.project.hello.city.plan.domain.model.Line

data class LineWithAccuracy(val line: Line, val accuracyLevel: AccuracyLevel) {

    val anyMatched = accuracyLevel != AccuracyLevel.NOT_MATCHED

    override fun toString(): String = "$line, $accuracyLevel"
}