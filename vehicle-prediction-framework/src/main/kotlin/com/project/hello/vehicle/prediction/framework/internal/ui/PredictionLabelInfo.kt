package com.project.hello.vehicle.prediction.framework.internal.ui

data class PredictionLabelInfo(
    val text: String?
) {
    companion object {
        val EMPTY = PredictionLabelInfo(text = null)
    }
}
