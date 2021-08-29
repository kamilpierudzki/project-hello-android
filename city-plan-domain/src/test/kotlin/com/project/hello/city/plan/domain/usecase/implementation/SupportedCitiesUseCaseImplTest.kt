package com.project.hello.city.plan.domain.usecase.implementation

import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.domain.model.SupportedCitiesData
import com.project.hello.city.plan.domain.model.api.CityPlanAPI
import com.project.hello.city.plan.domain.repository.CityPlanRepository
import com.project.hello.city.plan.domain.repository.resource.CityDataResource
import com.project.hello.city.plan.domain.usecase.SupportedCitiesUseCaseErrorMapper
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
internal class SupportedCitiesUseCaseImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val successfulResource: CityDataResource<CityPlan, CityPlanAPI> = mock {
        on { load(0) } doReturn ResponseApi.Success(createCityPlanApi(city = "A"))
        on { load(1) } doReturn ResponseApi.Success(createCityPlanApi(city = "B"))
    }

    val failureResource: CityDataResource<CityPlan, CityPlanAPI> = mock {
        on { load(any()) } doReturn ResponseApi.Error("error")
    }

    val repository: CityPlanRepository = mock {
        on { getSupportedCityFileResources() } doReturn listOf(0, 1)
    }

    val supportedCitiesUseCaseErrorMapper: SupportedCitiesUseCaseErrorMapper = mock()

    val tested = SupportedCitiesUseCaseImpl(
        repository,
        supportedCitiesUseCaseErrorMapper,
        coroutinesTestRule.testDispatcher
    )

    @Test
    fun `given successful fetching supported cities when execute is called then loading event followed by successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(repository.getCityDataResource()).thenReturn(successfulResource)

            // when
            val events = mutableListOf<Response<SupportedCitiesData>>()
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
            whenever(repository.getCityDataResource()).thenReturn(failureResource)

            // when
            val events = mutableListOf<Response<SupportedCitiesData>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Error)
        }

    private fun createCityPlanApi(city: String): CityPlanAPI = CityPlanAPI(
        city = city,
        lastUpdateTimestampInMillis = 0,
        humanReadableLastUpdateTimestamp = "",
        appVersion = "",
        trams = emptyList(),
        buses = emptyList()
    )
}