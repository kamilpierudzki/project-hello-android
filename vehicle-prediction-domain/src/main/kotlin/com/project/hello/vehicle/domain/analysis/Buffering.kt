package com.project.hello.vehicle.domain.analysis

import com.project.hello.city.plan.domain.model.Line

interface Buffering {

    fun bufferedLine(
        currentTimeInMillis: Long,
        newPrediction: Line?
    ): LineWithShare?
}

data class LineWithShare(
    val line: Line,
    val share: Int
) {
    init {
        if (share > 100 || share < 0) {
            throw IllegalStateException("Share value cannot be greater than 100 or less than 0. Current value $share")
        }
    }
}