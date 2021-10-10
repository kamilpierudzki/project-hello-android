package com.project.hello.vehicle.prediction.framework.internal.viewmodel

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.project.hello.commons.domain.test.CoroutinesTestRule
import com.project.hello.transit.agency.domain.VehicleType
import com.project.hello.transit.agency.domain.model.Line
import com.project.hello.transit.agency.domain.model.Stop
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.station.framework.api.LocationUseCase
import com.project.hello.transit.station.framework.api.TransitStationResult
import com.project.hello.transit.station.framework.api.TransitStationUseCase
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.Buffering
import com.project.hello.vehicle.domain.analysis.LineWithShare
import com.project.hello.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hello.vehicle.domain.timeout.TimeoutChecker
import com.project.hello.vehicle.domain.timeout.TimeoutCheckerFactory
import com.project.hello.vehicle.prediction.framework.internal.logger.PredictionConsoleLogger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
internal class PredictionViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    val tram1 = Line("T1", "")
    val tram2 = Line("T2", "")

    val bus1 = Line("B1", "")
    val bus2 = Line("B2", "")

    val transitAgency = TransitAgency(
        transitAgency = "A",
        lastUpdateFormatted = "",
        tramLines = listOf(tram1, tram2),
        busLines = listOf(bus1, bus2),
        tramStops = emptyList(),
        busStops = emptyList(),
    )
    val initialData = PredictionViewModelInitialData(
        listOf(VehicleType.TRAM, VehicleType.BUS),
        emptyMap(),
        transitAgency
    )

    val vehiclePrediction: VehiclePrediction = mock()
    val buffering: Buffering = mock()
    val countryCharactersEmitter: CountryCharactersEmitter = mock()
    val predictionConsoleLogger: PredictionConsoleLogger = mock()

    val timeoutChecker: TimeoutChecker = mock {
        on { isTimeout() } doReturn false
    }

    val timeoutCheckerFactory: TimeoutCheckerFactory = mock {
        on { create() } doReturn timeoutChecker
    }

    val mutableLocationUpdates = MutableLiveData<Location>()
    val locationUseCase: LocationUseCase = mock {
        on { locationUpdates } doReturn mutableLocationUpdates
    }

    val transitStationUseCase: TransitStationUseCase = mock()

    fun tested() = PredictionViewModel(
        vehiclePrediction,
        buffering,
        countryCharactersEmitter,
        coroutinesTestRule.testDispatcher,
        coroutinesTestRule.testDispatcher,
        predictionConsoleLogger,
        timeoutCheckerFactory,
        locationUseCase,
        transitStationUseCase,
    )

    @Test
    fun `when setInitialData is called then country characters are emitted`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // when
            tested.setInitialData(initialData)

            // then
            verify(countryCharactersEmitter).emmit(initialData.countryCharacters)
        }
    }

    @Test
    fun `given initial data is set when processRecognisedTexts is called then city lines are stored internally`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            tested.setInitialData(initialData)

            // when
            tested.processRecognisedTexts(listOf("a"))

            //  then
            argumentCaptor<List<Line>> {
                verify(vehiclePrediction).predictLine(any(), capture(), any())
                val cityLines: List<Line> = firstValue
                Assert.assertEquals(4, cityLines.size)
                Assert.assertEquals(cityLines[0].number, "T1")
                Assert.assertEquals(cityLines[1].number, "T2")
                Assert.assertEquals(cityLines[2].number, "B1")
                Assert.assertEquals(cityLines[3].number, "B2")
            }
        }
    }

    @Test
    fun `when processRecognisedTexts is called then vehicle prediction is called`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            tested.setInitialData(initialData)

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            verify(vehiclePrediction).predictLine(any(), any(), any())
        }
    }

    @Test
    fun `when processInput is called then predicted lines analysis in invoked`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            tested.setInitialData(initialData)

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            verify(buffering).bufferedLine(any(), anyOrNull())
        }
    }

    @Test
    fun `given observing predictedNumberLabel, previousPrediction is NULL and bufferedLine is NULL when processInput is called then events are sent accordingly`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.predictLine(any(), any(), any())).thenReturn(null)
            whenever(buffering.bufferedLine(any(), any())).thenReturn(null)

            val predictedNumberLabelEvents = mutableListOf<PredictionLabelInfo>()
            tested.predictedNumberLabel.observeForever { predictedNumberLabelEvents.add(it) }
            tested.setInitialData(initialData)
            predictedNumberLabelEvents.clear()

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            Assert.assertEquals(1, predictedNumberLabelEvents.size)
            Assert.assertEquals(PredictionLabelInfo.EMPTY, predictedNumberLabelEvents[0])
        }
    }

    @Test
    fun `given observing predictedNumberLabel, previousPrediction is NULL and bufferedLine is NOT NULL when processInput is called then events are sent accordingly`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.predictLine(any(), any(), any())).thenReturn(tram1)
            val lineWithProbability = LineWithShare(tram1, 99)
            whenever(buffering.bufferedLine(any(), any()))
                .thenReturn(lineWithProbability)

            val predictedNumberLabelEvents = mutableListOf<PredictionLabelInfo>()
            tested.predictedNumberLabel.observeForever { predictedNumberLabelEvents.add(it) }
            tested.setInitialData(initialData)
            predictedNumberLabelEvents.clear()

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            Assert.assertEquals(1, predictedNumberLabelEvents.size)
            Assert.assertEquals(tram1.number, predictedNumberLabelEvents[0].text)
        }
    }

    @Test
    fun `given observing predictedNumberLabel, previousPrediction is NOT NULL and bufferedLine is NULL when processInput is called then events are sent accordingly`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.predictLine(any(), any(), any())).thenReturn(tram1)
            val lineWithProbability = LineWithShare(tram1, 99)
            whenever(buffering.bufferedLine(any(), any()))
                .thenReturn(lineWithProbability)

            tested.setInitialData(initialData)
            tested.processRecognisedTexts(listOf("a")) // saving previous prediction

            val predictedNumberLabelEvents = mutableListOf<PredictionLabelInfo>()
            tested.predictedNumberLabel.observeForever { predictedNumberLabelEvents.add(it) }

            whenever(buffering.bufferedLine(any(), any())).thenReturn(null)

            predictedNumberLabelEvents.clear()

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            Assert.assertEquals(1, predictedNumberLabelEvents.size)
            Assert.assertEquals(PredictionLabelInfo.EMPTY, predictedNumberLabelEvents[0])
        }
    }

    @Test
    fun `given observing predictedNumberLabel, previousPrediction is NOT NULL and bufferedLine is NOT NULL when processInput is called then events are sent accordingly`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.predictLine(any(), any(), any())).thenReturn(tram1)
            val lineWithProbability = LineWithShare(tram1, 99)
            whenever(buffering.bufferedLine(any(), any()))
                .thenReturn(lineWithProbability)

            tested.setInitialData(initialData)
            tested.processRecognisedTexts(listOf("a")) // saving previous prediction

            val predictedNumberLabelEvents = mutableListOf<PredictionLabelInfo>()
            tested.predictedNumberLabel.observeForever { predictedNumberLabelEvents.add(it) }

            predictedNumberLabelEvents.clear()

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            Assert.assertEquals(0, predictedNumberLabelEvents.size)
        }
    }

    @Test
    fun `given confidence level is 84 when processInput is called then events are sent accordingly`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.predictLine(any(), any(), any())).thenReturn(tram1)
            val lineWithProbability = LineWithShare(tram1, 84)
            whenever(buffering.bufferedLine(any(), any()))
                .thenReturn(lineWithProbability)

            val predictedNumberLabelEvents = mutableListOf<PredictionLabelInfo>()
            tested.predictedNumberLabel.observeForever { predictedNumberLabelEvents.add(it) }
            tested.setInitialData(initialData)
            predictedNumberLabelEvents.clear()

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            Assert.assertEquals(0, predictedNumberLabelEvents.size)
        }
    }

    @Test
    fun `given initial data is provided when location update is received then transitStationUseCase is executed`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            tested.setInitialData(initialData)

            // when
            mutableLocationUpdates.postValue(Location(""))

            // then
            verify(transitStationUseCase).execute(any())
        }
    }

    @Test
    fun `given initial data is NOT provided when location update is received then transitStationUseCase is NOT executed`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given

            // when
            mutableLocationUpdates.postValue(Location(""))

            // then
            verify(transitStationUseCase, never()).execute(any())
        }
    }

    @Test
    fun `given initial data is provided and transit station result is returned when location update is received then city lines are updated`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val transitStationResult = TransitStationResult(
                tramStops = listOf(Stop("X", listOf(tram1.number))),
                busStops = emptyList(),
            )
            whenever(transitStationUseCase.execute(any()))
                .thenReturn(flow { emit(transitStationResult) })
            tested.setInitialData(initialData)

            // when
            mutableLocationUpdates.value = Location("")
            tested.processRecognisedTexts(listOf("a"))

            // then
            val argCaptor = argumentCaptor<List<Line>> {
                verify(vehiclePrediction).predictLine(any(), capture(), any())
            }
            Assert.assertEquals(listOf(tram1), argCaptor.firstValue)
        }
    }

    @Test
    fun `given observing city lines and transit station result is returned when initial data is provided then city lines are posted`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val transitStationResult = TransitStationResult(
                tramStops = listOf(Stop("X", listOf(tram1.number))),
                busStops = emptyList(),
            )
            whenever(transitStationUseCase.execute(any()))
                .thenReturn(flow { emit(transitStationResult) })
            val events = mutableListOf<List<Line>>()
            tested.cityLinesEvent.observeForever { events.add(it) }

            // when
            tested.setInitialData(initialData)

            // then
            Assert.assertEquals(1, events.size)
            Assert.assertEquals(4, events[0].size)
        }
    }

    @Test
    fun `given observing city lines and transit station result is returned when location update is updated then city lines are posted`() {
        val tested = tested()
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val transitStationResult = TransitStationResult(
                tramStops = listOf(Stop("X", listOf(tram1.number))),
                busStops = emptyList(),
            )
            whenever(transitStationUseCase.execute(any()))
                .thenReturn(flow { emit(transitStationResult) })
            val events = mutableListOf<List<Line>>()
            tested.cityLinesEvent.observeForever { events.add(it) }
            tested.setInitialData(initialData)

            // when
            events.clear()
            mutableLocationUpdates.value = Location("")

            // then
            Assert.assertEquals(1, events.size)
            Assert.assertEquals(1, events[0].size)
        }
    }
}