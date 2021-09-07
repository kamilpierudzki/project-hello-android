package com.project.hello.vehicle.prediction.framework.internal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.hello.city.plan.domain.VehicleType
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.domain.model.Line
import com.project.hello.commons.domain.test.CoroutinesTestRule
import com.project.hello.commons.framework.ui.IText
import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.domain.analysis.PredictedLinesAnalysis
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

    val cityPlan = CityPlan(
        city = "A",
        lastUpdateDate = "",
        listOf(Line("T1", ""), Line("T2", "")),
        listOf(Line("B1", ""), Line("B2", ""))
    )
    val initialData = PredictionViewModelInitialData(
        listOf(VehicleType.TRAM, VehicleType.BUS),
        emptyMap(),
        cityPlan
    )

    val vehiclePrediction: VehiclePrediction = mock()
    val predictedLinesAnalysis: PredictedLinesAnalysis = mock()
    val countryCharactersEmitter: CountryCharactersEmitter = mock()
    val predictionConsoleLogger: PredictionConsoleLogger = mock()

    val tested = PredictionViewModel(
        vehiclePrediction,
        predictedLinesAnalysis,
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
                verify(vehiclePrediction).processInput(any(), capture())
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
            verify(vehiclePrediction).processInput(any(), any())
        }

    @Test
    fun `when processInput is called then predicted lines analysis in invoked`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            tested.setInitialData(initialData)

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            verify(predictedLinesAnalysis).analysedSortedLines(any(), any())
        }

    @Test
    fun `given observing predicted lines updates when processInput is called then event predicted lines are updated`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val events = mutableListOf<List<LineWithProbability>>()
            tested.predictedLines.observeForever { events.add(it) }
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

    @Test
    fun `given observing predicted lines updates and predicted lines are recognised when processInput is called then correct number should be recognised`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val events = mutableListOf<List<LineWithProbability>>()
            tested.predictedLines.observeForever { events.add(it) }
            tested.setInitialData(initialData)

            whenever(vehiclePrediction.processInput(any(), any())).thenReturn(emptyList())

            val analysedLines = listOf(
                LineWithProbability(Line("A", "desA"), 0.3f),
                LineWithProbability(Line("B", "desA"), 0.1f),
                LineWithProbability(Line("A", "desB"), 0.3f)
            )
            whenever(predictedLinesAnalysis.analysedSortedLines(any(), any()))
                .thenReturn(analysedLines)

            // when
            tested.processRecognisedTexts(listOf("a"))

            // then
            Assert.assertEquals(1, events[0].size)
            Assert.assertEquals("A", events[0][0].line.number)
            Assert.assertEquals(0.6f, events[0][0].probability)
        }
}