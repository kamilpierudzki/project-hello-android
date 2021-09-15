package com.project.hello.vehicle.prediction.framework.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hello.city.plan.domain.VehicleType
import com.project.hello.city.plan.domain.model.Line
import com.project.hello.commons.framework.hilt.DefaultDispatcher
import com.project.hello.commons.framework.ui.Text
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.Buffering
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hello.vehicle.prediction.framework.internal.ui.PredictionLabelInfo
import com.project.hello.vehicle.prediction.framework.internal.ui.PredictionInfoTextCreation
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
    private val predictionInfoTextCreation: PredictionInfoTextCreation,
) : ViewModel() {

    private val cityLines = CopyOnWriteArrayList<Line>()
    private var previousPrediction: LineWithProbability? = null

    val predictedNumberLabel = MutableLiveData(PredictionLabelInfo.EMPTY)
    val predictedConfidenceInfo = MutableLiveData(PredictionLabelInfo.EMPTY)
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
                processBufferedLine(bufferedLine)
            }
    }

    private fun processBufferedLine(bufferedLine: LineWithProbability?) {
        when (val predictedLineResult = getPredictedLineResult(bufferedLine)) {
            PredictedLineResult.Negative -> handleNegativeResultOfCurrentPrediction()
            is PredictedLineResult.Positive ->
                handlePositiveResultOfCurrentPrediction(predictedLineResult)
        }
        previousPrediction = bufferedLine
    }

    private fun getPredictedLineResult(bufferedLine: LineWithProbability?): PredictedLineResult {
        return if (bufferedLine != null) {
            PredictedLineResult.Positive(bufferedLine)
        } else {
            PredictedLineResult.Negative
        }
    }

    private fun handleNegativeResultOfCurrentPrediction() {
        predictedNumberLabel.postValue(PredictionLabelInfo.EMPTY)
        predictedConfidenceInfo.postValue(PredictionLabelInfo.EMPTY)
    }

    private fun handlePositiveResultOfCurrentPrediction(currentResult: PredictedLineResult.Positive) {
        updatePredictedNumberLabelIfPossible(previousPrediction, currentResult)
        updatePredictedConfidenceInfo(currentResult.lineWithProbability)
    }

    private fun updatePredictedNumberLabelIfPossible(
        previousLine: LineWithProbability?,
        currentResult: PredictedLineResult.Positive
    ) {
        val isCurrentTheSameAsPrevious = isCurrentPredictionTheSameAsPrevious(
            previousPrediction = previousLine?.line,
            currentPrediction = currentResult.lineWithProbability.line
        )
        if (!isCurrentTheSameAsPrevious) {
            val labelInfo = predictionInfoTextCreation.createTextForNumberLabel(
                currentResult.lineWithProbability
            )
            predictedNumberLabel.postValue(labelInfo)
        }
    }

    private fun isCurrentPredictionTheSameAsPrevious(
        previousPrediction: Line?,
        currentPrediction: Line
    ): Boolean = previousPrediction == currentPrediction

    private fun updatePredictedConfidenceInfo(lineWithProbability: LineWithProbability) {
        val labelInfo = predictionInfoTextCreation.createTextForConfidenceLabel(
            lineWithProbability
        )
        predictedConfidenceInfo.postValue(labelInfo)
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