package com.project.halo.vehicle.prediction.framework.api

import androidx.lifecycle.LiveData
import com.project.halo.commons.viewmodel.ExternalViewModel
import com.project.halo.vehicle.domain.analysis.LineWithProbability

interface PredictionViewModel : ExternalViewModel {
    val predictedLines: LiveData<List<LineWithProbability>>

    fun processRecognisedTexts(input: List<String>)
}