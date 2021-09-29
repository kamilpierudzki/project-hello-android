package com.project.hello.vehicle.domain.analysis

import com.project.hello.city.plan.domain.model.Line

interface Buffering {

    fun bufferedLine(
        currentTimeInMillis: Long,
        newPrediction: Line?
    ): LineWithProbability?
}

data class LineWithProbability(
    val line: Line,
    val probability: Int
) {
    init {
        if (probability > 100 || probability < 0) {
            throw IllegalStateException("probability value cannot be greater than 100 or less than 0. Current value $probability")
        }
    }
}