package com.project.hello.vehicle.prediction.framework.internal.viewmodel

import android.location.Location
import androidx.lifecycle.*
import com.project.hello.commons.framework.hilt.DefaultDispatcher
import com.project.hello.commons.framework.hilt.IoDispatcher
import com.project.hello.transit.agency.domain.VehicleType
import com.project.hello.transit.agency.domain.model.Line
import com.project.hello.transit.agency.domain.model.Stop
import com.project.hello.vehicle.prediction.framework.internal.station.LocationUseCase
import com.project.hello.vehicle.prediction.framework.internal.station.TransitStationData
import com.project.hello.vehicle.prediction.framework.internal.station.TransitStationResult
import com.project.hello.vehicle.prediction.framework.internal.station.TransitStationUseCase
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.Buffering
import com.project.hello.vehicle.domain.analysis.LineWithShare
import com.project.hello.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hello.vehicle.domain.timeout.TimeoutCheckerFactory
import com.project.hello.vehicle.prediction.framework.internal.logger.PredictionConsoleLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

const val PREDICTION_CONFIDENCE_LEVEL_THRESHOLD = 85

@HiltViewModel
internal class PredictionViewModel @Inject constructor(
    private val vehiclePrediction: VehiclePrediction,
    private val buffering: Buffering,
    private val countryCharactersEmitter: CountryCharactersEmitter,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispather: CoroutineDispatcher,
    private val predictionConsoleLogger: PredictionConsoleLogger,
    private val timeoutCheckerFactory: TimeoutCheckerFactory,
    private val locationUseCase: LocationUseCase,
    private val transitStationUseCase: TransitStationUseCase,
) : ViewModel() {

    private val cityLines = CopyOnWriteArrayList<Line>()
    private val initialData = AtomicReference<PredictionViewModelInitialData>()

    private var previousPrediction: Line? = null

    private val locationObserver = Observer<Location> { location ->
        viewModelScope.launch(ioDispather) {
            initialData.get()?.selectedTransitAgency?.let { transitAgency ->
                val data = TransitStationData(
                    location = location,
                    selectedTransitAgency = transitAgency
                )
                transitStationUseCase.execute(data).collect { transitStationResult ->
                    updateAndPostCityLines(transitStationResult)
                    predictionConsoleLogger.cityLinesAreUpdated(cityLines.size)
                }
            }
        }
    }

    private val _cityLinesEvent = MutableLiveData<List<Line>>()

    val predictedNumberLabel = MutableLiveData(PredictionLabelInfo.EMPTY)
    val newFrame = MutableLiveData<Unit>()
    val locationSettingsSatisfactionEvent = locationUseCase.locationSettingsSatisfactionEvent
    val cityLinesEvent = _cityLinesEvent

    init {
        observeLocationUpdates()
    }

    override fun onCleared() {
        super.onCleared()
        locationUseCase.locationUpdates.removeObserver(locationObserver)
        locationUseCase.dispose()
    }

    fun setInitialData(initialData: PredictionViewModelInitialData) {
        countryCharactersEmitter.emmit(initialData.countryCharacters)
        this.initialData.set(initialData)
        updateAndPostCityLines(TransitStationResult.EMPTY)
    }

    fun processRecognisedTexts(inputs: List<String>) {
        predictionConsoleLogger.logUsedCityLines(cityLines)
        if (inputs.isNotEmpty() && cityLines.isNotEmpty()) {
            viewModelScope.launch(defaultDispatcher) {
                val input = inputs.reduce { acc, text -> "$acc$text" }
                predictionConsoleLogger.logRawRecognisedText(input)
                processInput(input)
            }
        }
    }

    fun startTransitStationModule() {
        locationUseCase.startLocationUpdates()
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

    private fun processBufferedLine(lineWithProbability: LineWithShare?) {
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

    private fun getPredictedLineResult(lineWithProbability: LineWithShare?): PredictedLineResult {
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

    private fun isConfidenceSatisfying(lineWithProbability: LineWithShare) =
        lineWithProbability.share >= PREDICTION_CONFIDENCE_LEVEL_THRESHOLD

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

    private fun updateAndPostCityLines(transitStationResult: TransitStationResult) {
        val initialData = initialData.get()
        if (initialData != null) {
            val vehicleTypeCityLines = initialData.targetVehicleTypes
                .flatMap {
                    when (it) {
                        VehicleType.TRAM -> initialData.selectedTransitAgency.tramLines
                        VehicleType.BUS -> initialData.selectedTransitAgency.busLines
                    }
                }

            val vehicleTransitStations = initialData.targetVehicleTypes
                .flatMap {
                    when (it) {
                        VehicleType.TRAM -> transitStationResult.tramStops
                        VehicleType.BUS -> transitStationResult.busStops
                    }
                }

            val filteredLines = vehicleTypeCityLines
                .asSequence()
                .filter {
                    shouldAllowLine(it, vehicleTransitStations)
                }
                .toList()


            if (filteredLines.isNotEmpty()) {
                cityLines.apply {
                    clear()
                    addAll(filteredLines)
                }
                _cityLinesEvent.postValue(cityLines)
            }
        }
    }

    private fun shouldAllowLine(line: Line, stops: List<Stop>): Boolean {
        if (stops.isEmpty()) {
            return true
        }

        for (stop in stops) {
            for (stopLine in stop.lines) {
                val isTheSame = line.number == stopLine
                if (isTheSame) {
                    return true
                }
            }
        }
        return false
    }

    private fun observeLocationUpdates() {
        locationUseCase.locationUpdates.observeForever(locationObserver)
    }
}