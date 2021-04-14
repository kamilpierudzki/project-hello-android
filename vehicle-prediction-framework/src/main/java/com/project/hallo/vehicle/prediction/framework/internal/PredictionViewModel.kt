package com.project.hallo.vehicle.prediction.framework.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.domain.usecase.CityPlanUseCase
import com.project.hallo.commons.framework.ui.Text
import com.project.hallo.vehicle.domain.VehiclePrediction
import com.project.hallo.vehicle.domain.analysis.LineWithProbability
import com.project.hallo.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.hallo.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hallo.vehicle.domain.steps.CountryCharactersProvider
import com.project.hallo.vehicle.prediction.framework.R
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

private const val NUM_OF_PREDICTED_LINES_TO_SHOW = 3

@HiltViewModel
internal class PredictionViewModel @Inject constructor(
    private val vehiclePrediction: VehiclePrediction,
    private val cityPlanUseCase: CityPlanUseCase,
    private val predictedLinesAnalysis: PredictedLinesAnalysis,
    private val countryCharactersEmitter: CountryCharactersEmitter
) : ViewModel() {

    private val cityLines = CopyOnWriteArrayList<Line>()

    val predictedLines = MutableLiveData<List<LineWithProbability>>()
    val screenContentDescription = MutableLiveData(Text.empty())

    fun setTargetVehicleType(initialData: PredictionViewModelInitialData) {
        // todo use async way
        cityLines.apply {
            clear()
            addAll(cityPlanUseCase.getCityPlan(initialData.targetVehicleTypes))
        }
        countryCharactersEmitter.emmit(initialData.countryCharacters)
    }

    fun processRecognisedTexts(inputs: List<String>) {
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