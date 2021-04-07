package com.project.halo.vehicle.prediction.framework.api

import androidx.lifecycle.LiveData
import com.project.hallo.city.plan.domain.Line
import com.project.halo.commons.viewmodel.ExternalViewModel

interface PredictionViewModel : ExternalViewModel {
    val predictedLines: LiveData<List<Line>>

    fun processRecognisedTexts(input: List<String>)
}