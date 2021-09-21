package com.project.hello.vehicle.prediction.framework.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hello.city.plan.domain.VehicleType
import com.project.hello.city.plan.domain.model.Line
import com.project.hello.commons.framework.hilt.DefaultDispatcher
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.Buffering
import com.project.hello.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hello.vehicle.prediction.framework.internal.ui.PredictionLabelInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

@HiltViewModel
internal class PredictionViewModel @Inject constructor(
    private val vehiclePrediction: VehiclePrediction,
    private val buffering: Buffering,
    private val countryCharactersEmitter: CountryCharactersEmitter,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val predictionConsoleLogger: PredictionConsoleLogger,
) : ViewModel() {

    private val cityLines = CopyOnWriteArrayList<Line>()
    private var previousPrediction: Line? = null

    val predictedNumberLabel = MutableLiveData(PredictionLabelInfo.EMPTY)
    val newFrame = MutableLiveData<Unit>()

    fun setInitialData(initialData: PredictionViewModelInitialData) {
        countryCharactersEmitter.emmit(initialData.countryCharacters)
        updateCityLines(initialData)
    }

    fun processRecognisedTexts(inputs: List<String>) {
        if (inputs.isNotEmpty() && cityLines.isNotEmpty()) {
            viewModelScope.launch(defaultDispatcher) {
                val input = inputs.reduce { acc, text -> "$acc$text" }
                predictionConsoleLogger.logRawRecognisedText(input)
                processInput(input)
            }
        }
    }

    private fun processInput(input: String) {
        val predictedLine = vehiclePrediction.mostProbableLine(input, cityLines)
        predictionConsoleLogger.logPredictedLine(predictedLine)

        val currentTimeInMillis = System.currentTimeMillis()
        buffering.bufferedLine(currentTimeInMillis, predictedLine)
            .also { bufferedLine ->
                predictionConsoleLogger.logBufferedLine(bufferedLine)
                newFrame.postValue(Unit)
                processBufferedLine(bufferedLine?.line)
            }
    }

    private fun processBufferedLine(bufferedLine: Line?) {
        when (val predictedLineResult = getPredictedLineResult(bufferedLine)) {
            PredictedLineResult.Negative -> handleNegativeResultOfCurrentPrediction()
            is PredictedLineResult.Positive ->
                handlePositiveResultOfCurrentPrediction(predictedLineResult)
        }
        previousPrediction = bufferedLine
    }

    private fun getPredictedLineResult(bufferedLine: Line?): PredictedLineResult {
        return if (bufferedLine != null) {
            PredictedLineResult.Positive(bufferedLine)
        } else {
            PredictedLineResult.Negative
        }
    }

    private fun handleNegativeResultOfCurrentPrediction() {
        predictedNumberLabel.postValue(PredictionLabelInfo.EMPTY)
    }

    private fun handlePositiveResultOfCurrentPrediction(currentResult: PredictedLineResult.Positive) {
        updatePredictedNumberLabelIfPossible(previousPrediction, currentResult)
    }

    private fun updatePredictedNumberLabelIfPossible(
        previousLine: Line?,
        currentResult: PredictedLineResult.Positive
    ) {
        val isCurrentTheSameAsPrevious = isCurrentPredictionTheSameAsPrevious(
            previousPrediction = previousLine,
            currentPrediction = currentResult.line
        )
        if (!isCurrentTheSameAsPrevious) {
            val labelInfo = PredictionLabelInfo(currentResult.line.number)
            predictedNumberLabel.postValue(labelInfo)
        }
    }

    private fun isCurrentPredictionTheSameAsPrevious(
        previousPrediction: Line?,
        currentPrediction: Line
    ): Boolean = previousPrediction == currentPrediction

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