package com.project.hello.vehicle.prediction.framework.internal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.hello.city.plan.domain.VehicleType
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.domain.model.Line
import com.project.hello.commons.domain.test.CoroutinesTestRule
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.Buffering
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hello.vehicle.prediction.framework.internal.ui.PredictionLabelInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    val cityPlan = CityPlan(
        city = "A",
        lastUpdateDate = "",
        listOf(tram1, tram2),
        listOf(bus1, bus2)
    )
    val initialData = PredictionViewModelInitialData(
        listOf(VehicleType.TRAM, VehicleType.BUS),
        emptyMap(),
        cityPlan
    )

    val vehiclePrediction: VehiclePrediction = mock()
    val buffering: Buffering = mock()
    val countryCharactersEmitter: CountryCharactersEmitter = mock()
    val predictionConsoleLogger: PredictionConsoleLogger = mock()

    val tested = PredictionViewModel(
        vehiclePrediction,
        buffering,
        countryCharactersEmitter,
        coroutinesTestRule.testDispatcher,
        predictionConsoleLogger
    )

    @Test
    fun `when setInitialData is called then country characters are emitted`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // when
            tested.setInitialData(initialData)

            // then
            verify(countryCharactersEmitter).emmit(initialData.countryCharacters)
        }

    @Test
    fun `given initial data is set when processRecognisedTexts is called then city lines are stored internally`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            tested.setInitialData(initialData)

            // when
            tested.processRecognisedTexts(listOf("a"))

            //  then
            argumentCaptor<List<Line>> {
                verify(vehiclePrediction).predictLine(any(), capture())
                val cityLines: List<Line> = firstValue
                Assert.assertEquals(4, cityLines.size)
                Assert.assertEquals(cityLines[0].number, "T1")
                Assert.assertEquals(cityLines[1].number, "T2")
                Assert.assertEquals(cityLines[2].number, "B1")
                Assert.assertEquals(cityLines[3].number, "B2")
            }
        }

    @Test
    fun `when processRecognisedTexts is called then vehicle prediction is called`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            tested.setInitialData(initialData)

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            verify(vehiclePrediction).predictLine(any(), any())
        }

    @Test
    fun `when processInput is called then predicted lines analysis in invoked`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            tested.setInitialData(initialData)

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            verify(buffering).bufferedLine(any(), anyOrNull())
        }

    @Test
    fun `given observing predictedNumberLabel, previousPrediction is NULL and bufferedLine is NULL when processInput is called then events are sent accordingly`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.predictLine(any(), any())).thenReturn(null)
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

    @Test
    fun `given observing predictedNumberLabel, previousPrediction is NULL and bufferedLine is NOT NULL when processInput is called then events are sent accordingly`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.predictLine(any(), any())).thenReturn(tram1)
            val lineWithProbability = LineWithProbability(tram1, 99)
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

    @Test
    fun `given observing predictedNumberLabel, previousPrediction is NOT NULL and bufferedLine is NULL when processInput is called then events are sent accordingly`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.predictLine(any(), any())).thenReturn(tram1)
            val lineWithProbability = LineWithProbability(tram1, 99)
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

    @Test
    fun `given observing predictedNumberLabel, previousPrediction is NOT NULL and bufferedLine is NOT NULL when processInput is called then events are sent accordingly`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.predictLine(any(), any())).thenReturn(tram1)
            val lineWithProbability = LineWithProbability(tram1, 99)
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

    @Test
    fun `given confidence level is 84 when processInput is called then events are sent accordingly`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.predictLine(any(), any())).thenReturn(tram1)
            val lineWithProbability = LineWithProbability(tram1, 84)
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