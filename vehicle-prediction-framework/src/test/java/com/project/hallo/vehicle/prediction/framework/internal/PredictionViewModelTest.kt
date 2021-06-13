package com.project.hallo.vehicle.prediction.framework.internal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.domain.model.Line
import com.project.hallo.city.plan.framework.internal.datamodel.CityPlanParcelable
import com.project.hallo.city.plan.framework.internal.datamodel.LineParcelable
import com.project.hallo.commons.domain.test.CoroutinesTestRule
import com.project.hallo.commons.framework.ui.IText
import com.project.hallo.vehicle.domain.VehiclePrediction
import com.project.hallo.vehicle.domain.analysis.LineWithProbability
import com.project.hallo.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.hallo.vehicle.domain.steps.CountryCharactersEmitter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
internal class PredictionViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    val cityPlan = CityPlanParcelable(
        "A",
        listOf(LineParcelable("T1", ""), LineParcelable("T2", "")),
        listOf(LineParcelable("B1", ""), LineParcelable("B2", ""))
    )
    val initialData = PredictionViewModelInitialData(
        listOf(VehicleType.TRAM, VehicleType.BUS),
        emptyMap(),
        cityPlan
    )

    val vehiclePrediction: VehiclePrediction = mock()
    val predictedLinesAnalysis: PredictedLinesAnalysis = mock()
    val countryCharactersEmitter: CountryCharactersEmitter = mock()

    val tested = PredictionViewModel(
        vehiclePrediction,
        predictedLinesAnalysis,
        countryCharactersEmitter,
        coroutinesTestRule.testDispatcher
    )

    // todo zweryfikowanie działania setInitialData(...)

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

    // todo zweryfikowanie działania processRecognisedTexts(...)

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
}