package com.project.hello.city.plan.domain.usecase.implementation

import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.domain.model.api.CityPlanAPI
import com.project.hello.city.plan.domain.model.api.LineAPI
import com.project.hello.city.plan.domain.repository.CityPlanRepository
import com.project.hello.city.plan.domain.repository.resource.CityDataResource
import com.project.hello.city.plan.domain.usecase.SelectedCityUseCaseErrorMapper
import com.project.hello.city.plan.domain.usecase.implementation.SelectedCityUseCaseImpl
import com.project.hello.commons.domain.data.Response
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
internal class SelectedCityUseCaseImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val cityPlan = CityPlan("a", emptyList(), emptyList())
    val cityPlanApi1 = CityPlanAPI(city = "a", trams = listOf(LineAPI("x", "y")))
    val cityPlanApi2 = CityPlanAPI(city = "b")
    val successfulResource: CityDataResource<CityPlan, CityPlanAPI> = mock {
        on { getCurrentlySelectedCity() } doReturn Response.Success(cityPlan)
        on { load(1) } doReturn Response.Success(cityPlanApi1)
        on { load(2) } doReturn Response.Success(cityPlanApi2)
    }
    val failedResource: CityDataResource<CityPlan, CityPlanAPI> = mock {
        on { getCurrentlySelectedCity() } doReturn Response.Error<CityPlan>("error")
    }

    val cityPlanRepository: CityPlanRepository = mock()
    val selectedCityUseCaseErrorMapper: SelectedCityUseCaseErrorMapper = mock()

    val tested = SelectedCityUseCaseImpl(
        cityPlanRepository,
        selectedCityUseCaseErrorMapper,
        coroutinesTestRule.testDispatcher
    )

    @Test
    fun `when execute is called then loading event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(cityPlanRepository.getCityDataResource()).thenReturn(failedResource)

            // when
            val events = mutableListOf<Response<CityPlan>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(true, events[0] is Response.Loading)
        }

    @Test
    fun `given fetching currently selected city fails when execute is called then loading event followed by failure event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(cityPlanRepository.getCityDataResource()).thenReturn(failedResource)

            // when
            val events = mutableListOf<Response<CityPlan>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Error)
        }

    @Test
    fun `given fetching currently selected city succeeds and fetching supported cities fails when execute is called then loading event followed by successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(cityPlanRepository.getCityDataResource()).thenReturn(successfulResource)
            whenever(cityPlanRepository.getSupportedCityFileResources()).thenReturn(emptyList())

            // when
            val events = mutableListOf<Response<CityPlan>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Success)
        }

    @Test
    fun `given fetching currently selected city succeeds and fetching supported cities succeeds when execute is called then loading event followed by successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(cityPlanRepository.getCityDataResource()).thenReturn(successfulResource)
            whenever(cityPlanRepository.getSupportedCityFileResources()).thenReturn(listOf(1, 2))

            // when
            val events = mutableListOf<Response<CityPlan>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Success)
            val selectedCityResponse = events[1] as Response.Success
            Assert.assertEquals(true, selectedCityResponse.successData.trams.size == 1)
        }
}