package com.project.hello.vehicle.prediction.framework.internal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.hello.city.plan.domain.VehicleType
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.domain.model.Line
import com.project.hello.commons.domain.test.CoroutinesTestRule
import com.project.hello.commons.framework.ui.IText
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.domain.analysis.Buffering
import com.project.hello.vehicle.domain.steps.CountryCharactersEmitter
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
                verify(vehiclePrediction).mostProbableLine(any(), capture())
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
            verify(vehiclePrediction).mostProbableLine(any(), any())
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
    fun `given observing predicted lines updates when processInput is called then event predicted lines are updated`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(vehiclePrediction.mostProbableLine(any(), any())).thenReturn(tram1)
            whenever(buffering.bufferedLine(any(), any()))
                .thenReturn(LineWithProbability(tram1, 0))
            val events = mutableListOf<PredictedLineEvent>()
            tested.predictedLineEvent.observeForever { events.add(it) }
            tested.setInitialData(initialData)

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            Assert.assertEquals(1, events.size)
        }

    @Test
    fun `given observing content description updates when processInput is called then screen content description is updated`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val events = mutableListOf<IText>()
            tested.screenContentDescription.observeForever { events.add(it) }
            tested.setInitialData(initialData)

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            Assert.assertEquals(1, events.size)
        }
}