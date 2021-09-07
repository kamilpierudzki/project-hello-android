package com.project.hello.vehicle.prediction.framework.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hello.city.plan.domain.VehicleType
import com.project.hello.city.plan.domain.model.Line
import com.project.hello.commons.framework.hilt.DefaultDispatcher
import com.project.hello.commons.framework.ui.Text
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.hello.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hello.vehicle.prediction.framework.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

private const val NUM_OF_PREDICTED_LINES_TO_SHOW = 1

@HiltViewModel
internal class PredictionViewModel @Inject constructor(
    private val vehiclePrediction: VehiclePrediction,
    private val predictedLinesAnalysis: PredictedLinesAnalysis,
    private val countryCharactersEmitter: CountryCharactersEmitter,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val predictionConsoleLogger: PredictionConsoleLogger
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
            .also {
                predictionConsoleLogger.logPredictedLines(it)
            }
            .let {
                mergedLinesWithTheSameNumber(it)
            }
            .take(NUM_OF_PREDICTED_LINES_TO_SHOW)
        lines.firstOrNull()?.let { updateScreenContentDescription(it) }
        this.predictedLines.postValue(lines)
    }

    private fun mergedLinesWithTheSameNumber(predicted: List<LineWithProbability>): List<LineWithProbability> {
        val result = hashMapOf<String, LineWithProbability>()
        for (line in predicted) {
            val lineFromResult: LineWithProbability? = result.getOrElse(line.line.number, { null })
            val probabilityFromResult: Float = lineFromResult?.probability ?: 0f
            val lineNumber = line.line.number

            result[lineNumber] = LineWithProbability(
                line = line.line.copy(destination = ""),
                probability = probabilityFromResult + line.probability
            )
        }

        return result.entries
            .sortedByDescending {
                it.value.probability
            }
            .map {
                it.value
            }
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