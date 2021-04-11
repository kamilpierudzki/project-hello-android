package com.project.halo.vehicle.prediction.framework.api

import androidx.lifecycle.LiveData
import com.project.halo.commons.viewmodel.ExternalViewModel
import com.project.halo.commons.viewmodel.ui.IText
import com.project.halo.vehicle.domain.analysis.LineWithProbability

interface PredictionViewModel : ExternalViewModel {
    val predictedLines: LiveData<List<LineWithProbability>>
    val screenContentDescription: LiveData<IText>

    fun processRecognisedTexts(inputs: List<String>)
}