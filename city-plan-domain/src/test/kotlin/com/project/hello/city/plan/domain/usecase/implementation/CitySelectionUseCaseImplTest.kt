package com.project.hello.city.plan.domain.usecase.implementation

import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.domain.model.api.CityPlanAPI
import com.project.hello.city.plan.domain.repository.CityPlanRepository
import com.project.hello.city.plan.domain.repository.resource.CityDataResource
import com.project.hello.city.plan.domain.usecase.implementation.CitySelectionUseCaseImpl
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
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
internal class CitySelectionUseCaseImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val cityPlan = createCityPlan("A")

    val cityDataResource: CityDataResource<CityPlan, CityPlanAPI> = mock()
    val cityPlanRepository: CityPlanRepository = mock {
        on { getCityDataResource() } doReturn cityDataResource
    }

    val tested = CitySelectionUseCaseImpl(cityPlanRepository, coroutinesTestRule.testDispatcher)

    @Test
    fun `when execute is called then loading event followed by successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // when
            val events = mutableListOf<Response<CityPlan>>()
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
            tested.execute(cityPlan).collect {}

            // then
            verify(cityDataResource).saveCurrentlySelectedCity(cityPlan)
        }

    @Test
    fun `given city plan when execute is called then the same city plan is sent is successful event`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // when
            val events = mutableListOf<Response<CityPlan>>()
            tested.execute(cityPlan).collect { events.add(it) }

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Success)
            val cityPlanResponse = events[1] as Response.Success<CityPlan>
            Assert.assertEquals(cityPlan, cityPlanResponse.successData)
        }

    private fun createCityPlan(city: String): CityPlan = CityPlan(
        city = city,
        lastUpdateDate = "",
        trams = emptyList(),
        buses = emptyList()
    )
}