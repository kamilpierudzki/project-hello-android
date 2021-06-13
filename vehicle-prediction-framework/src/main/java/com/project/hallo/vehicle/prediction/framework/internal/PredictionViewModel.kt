package com.project.hallo.vehicle.prediction.framework.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.domain.model.Line
import com.project.hallo.commons.framework.hilt.DefaultDispatcher
import com.project.hallo.commons.framework.ui.Text
import com.project.hallo.vehicle.domain.VehiclePrediction
import com.project.hallo.vehicle.domain.analysis.LineWithProbability
import com.project.hallo.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.hallo.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hallo.vehicle.prediction.framework.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

private const val NUM_OF_PREDICTED_LINES_TO_SHOW = 3

@HiltViewModel
internal class PredictionViewModel @Inject constructor(
    private val vehiclePrediction: VehiclePrediction,
    private val predictedLinesAnalysis: PredictedLinesAnalysis,
    private val countryCharactersEmitter: CountryCharactersEmitter,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val cityLines = CopyOnWriteArrayList<Line>()

    val predictedLines = MutableLiveData<List<LineWithProbability>>()
    val screenContentDescription = MutableLiveData(Text.empty())

    fun setInitialData(initialData: PredictionViewModelInitialData) {
        countryCharactersEmitter.emmit(initialData.countryCharacters)
        updateCityLines(initialData)
    }

    fun processRecognisedTexts(inputs: List<String>) {
        if (inputs.isNotEmpty() && cityLines.isNotEmpty()) {
            viewModelScope.launch(defaultDispatcher) {
                for (input in inputs) {
                    processInput(input)
                }
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

    private fun updateCityLines(initialData: PredictionViewModelInitialData) {
        val selectedCityLines: List<Line>? = initialData.targetVehicleTypes
            .takeIf { it.isNotEmpty() }
            ?.flatMap {
                when (it) {
                    VehicleType.TRAM -> initialData.selectedCity.trams
                    VehicleType.BUS -> initialData.selectedCity.buses
                }
            }
        if (selectedCityLines != null) {
            cityLines.apply {
                clear()
                addAll(selectedCityLines)
            }
        }
    }
}