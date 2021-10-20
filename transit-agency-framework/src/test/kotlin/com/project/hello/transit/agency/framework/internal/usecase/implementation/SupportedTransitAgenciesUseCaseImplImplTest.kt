package com.project.hello.transit.agency.framework.internal.usecase.implementation

import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.domain.test.CoroutinesTestRule
import com.project.hello.transit.agency.domain.model.SupportedTransitAgenciesData
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.domain.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.domain.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
internal class SupportedTransitAgenciesUseCaseImplImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val successfulRepository: TransitAgencyPlanRepository = mock {
        on { loadTransitAgencies() } doReturn listOf(createTransitAgency("A"), createTransitAgency("B"))
    }

    val failureRepository: TransitAgencyPlanRepository = mock {
        on { loadTransitAgencies() } doReturn emptyList()
    }

    val supportedTransitAgenciesUseCaseErrorMapper: SupportedTransitAgenciesUseCaseErrorMapper =
        mock()

    fun tested(repository: TransitAgencyPlanRepository) = SupportedTransitAgenciesUseCaseImpl(
        repository,
        supportedTransitAgenciesUseCaseErrorMapper,
        coroutinesTestRule.testDispatcher
    )

    @Test
    fun `given successful fetching supported cities when execute is called then loading event followed by successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val tested = tested(successfulRepository)

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
            val tested = tested(failureRepository)

            // when
            val events = mutableListOf<Response<SupportedTransitAgenciesData>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Error)
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