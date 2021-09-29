package com.project.hello.vehicle.prediction.framework.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hello.city.plan.domain.VehicleType
import com.project.hello.city.plan.domain.model.Line
import com.project.hello.commons.framework.hilt.DefaultDispatcher
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.Buffering
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hello.vehicle.prediction.framework.internal.timeoutchecker.TimeoutCheckerFactory
import com.project.hello.vehicle.prediction.framework.internal.ui.PredictionLabelInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

const val PREDICTION_CONFIDENCE_LEVEL_THRESHOLD = 85

@HiltViewModel
internal class PredictionViewModel @Inject constructor(
    private val vehiclePrediction: VehiclePrediction,
    private val buffering: Buffering,
    private val countryCharactersEmitter: CountryCharactersEmitter,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val predictionConsoleLogger: PredictionConsoleLogger,
    private val timeoutCheckerFactory: TimeoutCheckerFactory,
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
        val timeoutChecker = timeoutCheckerFactory.create()
        val predictedLine = vehiclePrediction.predictLine(input, cityLines, timeoutChecker)
        predictionConsoleLogger.logPredictedLine(predictedLine)

        val currentTimeInMillis = System.currentTimeMillis()
        buffering.bufferedLine(currentTimeInMillis, predictedLine)
            .also { bufferedLine ->
                predictionConsoleLogger.logBufferedLine(bufferedLine)
                newFrame.postValue(Unit)
                processBufferedLine(bufferedLine)
            }
    }

    private fun processBufferedLine(lineWithProbability: LineWithProbability?) {
        when (val predictedLineResult = getPredictedLineResult(lineWithProbability)) {
            PredictedLineResult.Negative ->
                handleNegativeResultOfCurrentPrediction()
            is PredictedLineResult.Positive ->
                handlePositiveResultOfCurrentPrediction(predictedLineResult)
            PredictedLineResult.ConfidenceTooLow ->
                return
        }
        previousPrediction = lineWithProbability?.line
    }

    private fun getPredictedLineResult(lineWithProbability: LineWithProbability?): PredictedLineResult {
        return if (lineWithProbability != null) {
            if (isConfidenceSatisfying(lineWithProbability)) {
                PredictedLineResult.Positive(lineWithProbability.line)
            } else {
                PredictedLineResult.ConfidenceTooLow
            }
        } else {
            PredictedLineResult.Negative
        }
    }

    private fun isConfidenceSatisfying(lineWithProbability: LineWithProbability) =
        lineWithProbability.probability >= PREDICTION_CONFIDENCE_LEVEL_THRESHOLD

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