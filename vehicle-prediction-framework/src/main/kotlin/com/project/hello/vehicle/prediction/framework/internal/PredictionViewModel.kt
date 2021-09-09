package com.project.hello.vehicle.prediction.framework.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hello.city.plan.domain.VehicleType
import com.project.hello.city.plan.domain.model.Line
import com.project.hello.commons.framework.hilt.DefaultDispatcher
import com.project.hello.commons.framework.ui.IText
import com.project.hello.commons.framework.ui.Text
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.domain.analysis.Buffering
import com.project.hello.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hello.vehicle.prediction.framework.R
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
    private val predictionConsoleLogger: PredictionConsoleLogger
) : ViewModel() {

    private val cityLines = CopyOnWriteArrayList<Line>()

    val predictedLineEvent = MutableLiveData<PredictedLineEvent>()
    val screenContentDescription = MutableLiveData<IText>()

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
        val predictedLine = vehiclePrediction.mostProbableLine(input, cityLines)
        predictionConsoleLogger.logPredictedLine(predictedLine)

        val currentTimeInMillis = System.currentTimeMillis()
        buffering.bufferedLine(currentTimeInMillis, predictedLine)
            .also { bufferedLine ->
                predictionConsoleLogger.logBufferedLine(bufferedLine)
                updateScreenContentDescription(bufferedLine?.line)
                postBufferedLine(bufferedLine)
            }
    }

    private fun postBufferedLine(bufferedLine: LineWithProbability?) {
        val valueToPost = if (bufferedLine != null) {
            PredictedLineEvent.Positive(bufferedLine)
        } else {
            PredictedLineEvent.Negative
        }
        predictedLineEvent.postValue(valueToPost)
    }

    private fun updateScreenContentDescription(line: Line?) {
        val contentDescription = if (line != null) {
            Text.of(listOf(Text.of(R.string.probably), Text.of(line.number)), separator = " ")
        } else {
            Text.empty()
        }
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