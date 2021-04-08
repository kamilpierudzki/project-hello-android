package com.project.halo.vehicle.prediction.framework.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hallo.city.plan.data.CityPlanUseCase
import com.project.halo.vehicle.domain.VehiclePrediction
import com.project.halo.vehicle.domain.analysis.LineWithProbability
import com.project.halo.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.halo.vehicle.prediction.framework.api.PredictionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val NUM_OF_PREDICTED_LINES_TO_SHOW = 3

@HiltViewModel
class PredictionViewModelImpl @Inject constructor(
    private val vehiclePrediction: VehiclePrediction,
    private val cityPlanUseCase: CityPlanUseCase,
    private val predictedLinesAnalysis: PredictedLinesAnalysis
) : ViewModel(), PredictionViewModel {

    override val predictedLines = MutableLiveData<List<LineWithProbability>>()

    override fun processRecognisedTexts(input: List<String>) {
        for (line in input) {
            val predictedLines = vehiclePrediction.processInput(line, cityPlanUseCase.getCityPlan())
            val currentTimeInMillis = System.currentTimeMillis()
            predictedLinesAnalysis.analysedLines(predictedLines, currentTimeInMillis) {
                this.predictedLines.postValue(it.take(NUM_OF_PREDICTED_LINES_TO_SHOW))
            }
        }
    }
}