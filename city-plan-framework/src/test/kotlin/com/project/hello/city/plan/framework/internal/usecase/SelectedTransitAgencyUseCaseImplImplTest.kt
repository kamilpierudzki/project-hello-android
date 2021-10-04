package com.project.hello.city.plan.framework.internal.usecase

import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.city.plan.framework.internal.model.api.LineAPI
import com.project.hello.city.plan.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.city.plan.framework.internal.repository.TransitAgencyPlanRepository
import com.project.hello.city.plan.framework.internal.repository.TransitAgencyDataResource
import com.project.hello.transit.agency.domain.usecase.SelectedTransitAgencyUseCaseErrorMapper
import com.project.hello.city.plan.framework.internal.usecase.implementation.SelectedTransitAgencyUseCaseImpl
import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.domain.test.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
internal class SelectedTransitAgencyUseCaseImplImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val transitAgency = TransitAgency("a", "", emptyList(), emptyList())
    val transitAgencyApi1 = createTransitAgencyApi(
        transitAgency = "a",
        trams = listOf(LineAPI("x", "y"))
    )
    val transitAgencyApi2 = createTransitAgencyApi("b")
    val successfulResource: TransitAgencyDataResource = mock {
        on { getCurrentlySelectedTransitAgency() } doReturn ResponseApi.Success(transitAgency)
        on { load(1) } doReturn ResponseApi.Success(transitAgencyApi1)
        on { load(2) } doReturn ResponseApi.Success(transitAgencyApi2)
    }
    val failedResource: TransitAgencyDataResource = mock {
        on { getCurrentlySelectedTransitAgency() } doReturn ResponseApi.Error("error")
    }

    val transitAgencyPlanRepository: TransitAgencyPlanRepository = mock()
    val selectedTransitAgencyUseCaseErrorMapper: SelectedTransitAgencyUseCaseErrorMapper = mock()

    val tested = SelectedTransitAgencyUseCaseImpl(
        transitAgencyPlanRepository,
        selectedTransitAgencyUseCaseErrorMapper,
        coroutinesTestRule.testDispatcher
    )

    @Test
    fun `when execute is called then loading event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(transitAgencyPlanRepository.getTransitAgencyDataResource()).thenReturn(
                failedResource
            )

            // when
            val events = mutableListOf<Response<TransitAgency>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(true, events[0] is Response.Loading)
        }

    @Test
    fun `given fetching currently selected city fails when execute is called then loading event followed by failure event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(transitAgencyPlanRepository.getTransitAgencyDataResource()).thenReturn(
                failedResource
            )

            // when
            val events = mutableListOf<Response<TransitAgency>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Error)
        }

    @Test
    fun `given fetching currently selected city succeeds and fetching supported cities fails when execute is called then loading event followed by successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(transitAgencyPlanRepository.getTransitAgencyDataResource()).thenReturn(
                successfulResource
            )
            whenever(transitAgencyPlanRepository.getSupportedTransitAgenciesFileResources()).thenReturn(
                emptyList()
            )

            // when
            val events = mutableListOf<Response<TransitAgency>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Success)
        }

    @Test
    fun `given fetching currently selected city succeeds and fetching supported cities succeeds when execute is called then loading event followed by successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(transitAgencyPlanRepository.getTransitAgencyDataResource()).thenReturn(
                successfulResource
            )
            whenever(transitAgencyPlanRepository.getSupportedTransitAgenciesFileResources()).thenReturn(
                listOf(1, 2)
            )

            // when
            val events = mutableListOf<Response<TransitAgency>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Success)
            val selectedCityResponse = events[1] as Response.Success
            Assert.assertEquals(true, selectedCityResponse.successData.trams.size == 1)
        }

    private fun createTransitAgencyApi(
        transitAgency: String,
        trams: List<LineAPI> = emptyList()
    ): TransitAgencyAPI = TransitAgencyAPI(
        transitAgency = transitAgency,
        lastUpdateTimestampInMillis = 0,
        lastUpdateFormatted = "",
        dataVersion = 1,
        trams = trams,
        buses = emptyList()
    )
}