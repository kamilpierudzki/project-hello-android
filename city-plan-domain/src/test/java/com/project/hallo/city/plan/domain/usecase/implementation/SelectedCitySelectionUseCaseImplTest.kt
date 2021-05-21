package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.api.CityPlanAPI
import com.project.hallo.city.plan.domain.repository.CityPlanRepository
import com.project.hallo.city.plan.domain.repository.resource.CityDataResource
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.domain.test.CoroutinesTestRule
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
internal class SelectedCitySelectionUseCaseImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val successfulResource: CityDataResource<CityPlan, CityPlanAPI> = mock {
        on { getCurrentlySelectedCity() } doReturn Response.Success(
            CityPlan("A", emptyList(), emptyList())
        )
    }

    val failureResource: CityDataResource<CityPlan, CityPlanAPI> = mock {
        on { getCurrentlySelectedCity() } doReturn Response.Error<CityPlan>("Couldn't load selected city")
    }

    val repository: CityPlanRepository = mock()

    val tested = SelectedCityUseCaseImpl(repository, coroutinesTestRule.testDispatcher)

    @Test
    fun `given city is selected when execute is called then loading event followed by successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(repository.getCityDataResource()).thenReturn(successfulResource)

            // when
            val events = mutableListOf<Response<CityPlan>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Success)
        }

    @Test
    fun `given city is not selected when execute us called then loading event followed by failure event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(repository.getCityDataResource()).thenReturn(failureResource)

            // when
            val events = mutableListOf<Response<CityPlan>>()
            tested.execute().collect { events.add(it) }

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0] is Response.Loading)
            Assert.assertEquals(true, events[1] is Response.Error)
        }
}