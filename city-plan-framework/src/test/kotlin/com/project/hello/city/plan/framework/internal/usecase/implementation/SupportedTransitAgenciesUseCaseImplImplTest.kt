package com.project.hello.city.plan.framework.internal.usecase.implementation

import com.project.hello.city.plan.domain.model.SupportedTransitAgenciesData
import com.project.hello.city.plan.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.city.plan.framework.internal.repository.TransitAgencyPlanRepository
import com.project.hello.city.plan.framework.internal.repository.TransitAgencyDataResource
import com.project.hello.city.plan.domain.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.domain.test.CoroutinesTestRule
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
        on { load(0) } doReturn ResponseApi.Success(createTransitAgencyApi("A"))
        on { load(1) } doReturn ResponseApi.Success(createTransitAgencyApi("B"))
    }

    val failureResource: TransitAgencyDataResource = mock {
        on { load(any()) } doReturn ResponseApi.Error("error")
    }

    val repository: TransitAgencyPlanRepository = mock {
        on { getSupportedTransitAgenciesFileResources() } doReturn listOf(0, 1)
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
}