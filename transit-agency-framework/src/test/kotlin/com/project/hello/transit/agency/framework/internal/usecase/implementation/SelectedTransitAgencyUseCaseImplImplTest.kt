package com.project.hello.transit.agency.framework.internal.usecase.implementation

import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.domain.test.CoroutinesTestRule
import com.project.hello.transit.agency.domain.model.Line
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.domain.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.domain.usecase.SelectedTransitAgencyUseCaseErrorMapper
import com.project.hello.transit.agency.framework.internal.model.api.LineAPI
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
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

    val transitAgency = TransitAgency(
        "a", "",
        emptyList(), emptyList(), emptyList(), emptyList()
    )
    val transitAgency1 = createTransitAgency(
        transitAgency = "a",
        trams = listOf(Line("x", "y"))
    )
    val transitAgency2 = createTransitAgency("b")

    val successfulRepository: TransitAgencyPlanRepository = mock {
        on { getCurrentlySelectedTransitAgency() } doReturn ResponseApi.Success(transitAgency)
        on { loadTransitAgencies() } doReturn listOf(transitAgency1, transitAgency2)
    }
    val failedRepository: TransitAgencyPlanRepository = mock {
        on { getCurrentlySelectedTransitAgency() } doReturn ResponseApi.Error("error")
    }

    val selectedTransitAgencyUseCaseErrorMapper: SelectedTransitAgencyUseCaseErrorMapper = mock()

    fun tested(repository: TransitAgencyPlanRepository) = SelectedTransitAgencyUseCaseImpl(
        repository,
        selectedTransitAgencyUseCaseErrorMapper,
        coroutinesTestRule.testDispatcher
    )

    @Test
    fun `when execute is called then loading event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val tested = tested(failedRepository)

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
            val tested = tested(failedRepository)

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
            whenever(successfulRepository.loadTransitAgencies()).thenReturn(emptyList())
            val tested = tested(successfulRepository)

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
            whenever(successfulRepository.loadTransitAgencies()).thenReturn(
                listOf(
                    transitAgency1,
                    transitAgency2
                )
            )
            val tested = tested(successfulRepository)

            // when
            val events = mutableListOf<Response<TransitAgency>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Success)
            val selectedCityResponse = events[1] as Response.Success
            Assert.assertEquals(1, selectedCityResponse.successData.tramLines.size)
        }

    private fun createTransitAgency(
        transitAgency: String,
        trams: List<Line> = emptyList()
    ) = TransitAgency(
        transitAgency = transitAgency,
        lastUpdateFormatted = "",
        tramLines = trams,
        busLines = emptyList(),
        tramStops = emptyList(),
        busStops = emptyList()
    )

    private fun createTransitAgencyApi(
        transitAgency: String,
        trams: List<LineAPI> = emptyList()
    ): TransitAgencyAPI = TransitAgencyAPI(
        transitAgency = transitAgency,
        lastUpdateTimestampInMillis = 0,
        lastUpdateFormatted = "",
        dataVersion = 1,
        tramLines = trams,
        busLines = emptyList(),
        tramStops = emptyList(),
        busStops = emptyList()
    )
}