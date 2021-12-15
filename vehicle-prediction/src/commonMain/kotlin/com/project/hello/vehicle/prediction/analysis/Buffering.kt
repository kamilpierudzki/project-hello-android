package com.project.hello.vehicle.prediction.analysis

import com.project.hello.transit.agency.model.Line

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
        if (share !in 0..100) {
            throw IllegalArgumentException("Share value cannot be greater than 100 or less than 0. Current value $share")
        }
    }
}