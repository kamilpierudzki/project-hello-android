package com.project.hello.transit.agency.framework.internal.usecase.implementation

import com.project.hello.transit.agency.domain.model.SupportedTransitAgenciesData
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.transit.agency.framework.internal.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.framework.internal.repository.TransitAgencyDataResource
import com.project.hello.transit.agency.domain.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.domain.test.CoroutinesTestRule
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyStopAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
internal class SupportedTransitAgenciesUseCaseImplImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val successfulResource: TransitAgencyDataResource = mock {
        on { loadTransitAgency(0) } doReturn ResponseApi.Success(createTransitAgencyApi("A"))
        on { loadTransitAgency(1) } doReturn ResponseApi.Success(createTransitAgencyApi("B"))
        on { loadTransitAgencyStop(0) } doReturn ResponseApi.Success(createTransitAgencyStopAPI("X"))
        on { loadTransitAgencyStop(1) } doReturn ResponseApi.Success(createTransitAgencyStopAPI("Y"))
    }

    val failureResource: TransitAgencyDataResource = mock {
        on { loadTransitAgency(any()) } doReturn ResponseApi.Error("error")
    }

    val repository: TransitAgencyPlanRepository = mock {
        on { getSupportedTransitAgenciesFileResources() } doReturn listOf(0, 1)
        on { getSupportedTransitAgencyStopsFileResources() } doReturn listOf(0, 1)
    }

    val supportedTransitAgenciesUseCaseErrorMapper: SupportedTransitAgenciesUseCaseErrorMapper =
        mock()

    val tested = SupportedTransitAgenciesUseCaseImpl(
        repository,
        supportedTransitAgenciesUseCaseErrorMapper,
        coroutinesTestRule.testDispatcher
    )

    @Test
    fun `given successful fetching supported cities when execute is called then loading event followed by successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(repository.getTransitAgencyDataResource()).thenReturn(successfulResource)

            // when
            val events = mutableListOf<Response<SupportedTransitAgenciesData>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Success)
        }

    @Test
    fun `given failed fetching supported cities when execute is called then loading event followed by failure event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(repository.getTransitAgencyDataResource()).thenReturn(failureResource)

            // when
            val events = mutableListOf<Response<SupportedTransitAgenciesData>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Error)
        }

    private fun createTransitAgencyApi(transitAgency: String): TransitAgencyAPI = TransitAgencyAPI(
        transitAgency = transitAgency,
        lastUpdateTimestampInMillis = 0,
        lastUpdateFormatted = "",
        dataVersion = 1,
        trams = emptyList(),
        buses = emptyList()
    )

    private fun createTransitAgencyStopAPI(transitAgency: String) = TransitAgencyStopAPI(
        transitAgency = transitAgency,
        lastUpdateTimestampInMillis = 1,
        lastUpdateFormatted = "",
        dataVersion = 1,
        tramStops = emptyList(),
        busStops = emptyList()
    )
}