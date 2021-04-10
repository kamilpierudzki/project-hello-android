package com.project.halo.vehicle.prediction.framework.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hallo.city.plan.domain.usecase.CityPlanUseCase
import com.project.halo.vehicle.domain.VehiclePrediction
import com.project.halo.vehicle.domain.analysis.LineWithProbability
import com.project.halo.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.halo.vehicle.prediction.framework.api.PredictionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val NUM_OF_PREDICTED_LINES_TO_SHOW = 3

@HiltViewModel
internal class PredictionViewModelImpl @Inject constructor(
    private val vehiclePrediction: VehiclePrediction,
    private val cityPlanUseCase: CityPlanUseCase,
    private val predictedLinesAnalysis: PredictedLinesAnalysis
) : ViewModel(), PredictionViewModel {

    override val predictedLines = MutableLiveData<List<LineWithProbability>>()

    override val screenContentDescription = MutableLiveData("")

    override fun processRecognisedTexts(input: List<String>) {
        for (line in input) {
            val predictedLines = vehiclePrediction.processInput(line, cityPlanUseCase.getCityPlan())
            val currentTimeInMillis = System.currentTimeMillis()
            val lines = predictedLinesAnalysis
                .analysedSortedLines(predictedLines, currentTimeInMillis)
                .take(NUM_OF_PREDICTED_LINES_TO_SHOW)
            lines.firstOrNull()?.let { updateScreenContentDescription(it) }
            this.predictedLines.postValue(lines)
        }
    }

    private fun updateScreenContentDescription(data: LineWithProbability) {
        val contentDescription = "Prawdopodobnie ${data.line.number}, ${data.line.destination}"
        screenContentDescription.postValue(contentDescription)
    }
}