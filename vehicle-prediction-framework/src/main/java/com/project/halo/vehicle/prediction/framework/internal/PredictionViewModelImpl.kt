package com.project.halo.vehicle.prediction.framework.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.usecase.CityPlanUseCase
import com.project.halo.commons.viewmodel.ui.Text
import com.project.halo.vehicle.domain.VehiclePrediction
import com.project.halo.vehicle.domain.analysis.LineWithProbability
import com.project.halo.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.halo.vehicle.prediction.framework.R
import com.project.halo.vehicle.prediction.framework.api.PredictionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

private const val NUM_OF_PREDICTED_LINES_TO_SHOW = 3

@HiltViewModel
internal class PredictionViewModelImpl @Inject constructor(
    private val vehiclePrediction: VehiclePrediction,
    private val cityPlanUseCase: CityPlanUseCase,
    private val predictedLinesAnalysis: PredictedLinesAnalysis
) : ViewModel(), PredictionViewModel {

    private val cityLines = CopyOnWriteArrayList<Line>()

    override val predictedLines = MutableLiveData<List<LineWithProbability>>()
    override val screenContentDescription = MutableLiveData(Text.empty())

    init {
        cityLines.addAll(cityPlanUseCase.getCityPlan())
    }

    override fun processRecognisedTexts(inputs: List<String>) {
        if (inputs.isNotEmpty() && cityLines.isNotEmpty()) {
            for (input in inputs) {
                processInput(input)
            }
        }
    }

    private fun processInput(input: String) {
        val predictedLines = vehiclePrediction.processInput(input, cityLines)
        val currentTimeInMillis = System.currentTimeMillis()
        val lines = predictedLinesAnalysis
            .analysedSortedLines(predictedLines, currentTimeInMillis)
            .take(NUM_OF_PREDICTED_LINES_TO_SHOW)
        lines.firstOrNull()?.let { updateScreenContentDescription(it) }
        this.predictedLines.postValue(lines)
    }

    private fun updateScreenContentDescription(data: LineWithProbability) {
        val contentDescription = Text.of(
            listOf(
                Text.of(R.string.probably),
                Text.of("${data.line.number}, ${data.line.destination}")
            ),
            separator = " "
        )
        screenContentDescription.postValue(contentDescription)
    }
}