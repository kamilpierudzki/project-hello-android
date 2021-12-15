package com.project.hello.vehicle.prediction.internal.viewmodel

data class PredictionLabelInfo(
    val text: String?
) {
    companion object {
        val EMPTY = PredictionLabelInfo(text = null)
    }
}
