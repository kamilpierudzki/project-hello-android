package com.project.hello.transit.agency.internal.usecase.implementation

import com.project.hello.commons.data.Response
import com.project.hello.commons.test.CoroutinesTestRule
import com.project.hello.transit.agency.model.TransitAgency
import com.project.hello.transit.agency.repository.TransitAgencyPlanRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
internal class TransitAgencySelectionUseCaseImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val cityPlan = createTransitAgency("A")

    val transitAgencyPlanRepository: TransitAgencyPlanRepository = mock()

    val tested = TransitAgencySelectionUseCaseImpl(
        transitAgencyPlanRepository,
        coroutinesTestRule.testDispatcher
    )

    @Test
    fun `when execute is called then loading event followed by successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // when
            val events = mutableListOf<Response<TransitAgency>>()
            tested.execute(cityPlan).collect { events.add(it) }

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Success)
        }

    @Test
    fun `given city plan when execute is called then city plan is saved`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // when
            tested.execute(cityPlan).collect { }

            // then
            verify(transitAgencyPlanRepository).saveCurrentlySelectedTransitAgency(cityPlan)
        }

    @Test
    fun `given city plan when execute is called then the same city plan is sent is successful event`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // when
            val events = mutableListOf<Response<TransitAgency>>()
            tested.execute(cityPlan).collect { events.add(it) }

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Success)
            val cityPlanResponse = events[1] as Response.Success<TransitAgency>
            Assert.assertEquals(cityPlan, cityPlanResponse.successData)
        }

    private fun createTransitAgency(transitAgency: String) = TransitAgency(
        transitAgency = transitAgency,
        lastUpdateFormatted = "",
        tramLines = emptyList(),
        busLines = emptyList(),
        tramStops = emptyList(),
        busStops = emptyList(),
    )
}