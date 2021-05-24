package com.project.hallo.city.plan.framework.internal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.SupportedCitiesData
import com.project.hallo.city.plan.domain.usecase.CitySelectionUseCase
import com.project.hallo.city.plan.domain.usecase.SelectedCityUseCase
import com.project.hallo.city.plan.domain.usecase.SupportedCitiesUseCase
import com.project.hallo.city.plan.framework.api.CitySelection
import com.project.hallo.city.plan.framework.api.SupportedCitiesStatus
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.domain.test.CoroutinesTestRule
import com.project.hallo.commons.framework.livedata.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
internal class CityPickViewModelImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    val cityA = CityPlan("A", emptyList(), emptyList())
    val cityB = CityPlan("B", emptyList(), emptyList())

    val supportedCitiesUseCase: SupportedCitiesUseCase = mock()
    val citySelectionUseCase: CitySelectionUseCase = mock()
    val selectedCityUseCase: SelectedCityUseCase = mock {
        on { execute() } doReturn flow {
            emit(Response.Loading<CityPlan>())
        }
    }

    @Test
    fun `given city is selected when view model is initialised then proper use case is called`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedCityUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Success(cityA))
                }
            )

            // when
            tested()

            // then
            verify(selectedCityUseCase).execute()
        }

    @Test
    fun `given city is selected when view model is initialised then successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedCityUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Success(cityA))
                    emit(Response.Success(cityB))
                }
            )

            // when
            val tested = tested(selectedCityUseCase = selectedCityUseCase)
            lateinit var selection: CitySelection.Selected
            tested.currentlySelectedCity.observeForever {
                selection = it.getContentOrNull() as CitySelection.Selected
            }

            // then
            verify(selectedCityUseCase).execute()
            Assert.assertEquals("B", selection.cityPlan.city)
        }

    @Test
    fun `given city is not selected when view model is initialised then failure event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedCityUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Error<CityPlan>("empty"))
                }
            )

            // when
            val tested = tested(selectedCityUseCase = selectedCityUseCase)
            lateinit var selection: CitySelection
            tested.currentlySelectedCity.observeForever {
                selection = it.content
            }

            // then
            verify(selectedCityUseCase).execute()
            Assert.assertEquals(true, selection is CitySelection.NotSelected)
        }

    @Test
    fun `given observing supported cities and successful fetching supported cities when supported cities are being fetch then loading status followed by successful status is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(supportedCitiesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(SupportedCitiesData(listOf(cityA, cityB))))
                }
            )
            val tested = tested()
            val statuses = mutableListOf<Event<SupportedCitiesStatus>>()
            tested.supportedCities.observeForever { statuses.add(it) }

            // when
            tested.forceFetchSupportedCities()

            // then
            verify(supportedCitiesUseCase).execute()
            Assert.assertEquals(1, statuses.size)
            Assert.assertEquals(true, statuses[0].content is SupportedCitiesStatus.Success)
            val second = statuses[0].content as SupportedCitiesStatus.Success
            Assert.assertEquals(2, second.supportedCities.size)
        }

    @Test
    fun `given observing supported cities, successful fetching supported cities and currently selected city is available when supported cities are being fetch then loading status followed by successful status is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedCityUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Success(cityA))
                }
            )
            whenever(supportedCitiesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(SupportedCitiesData(listOf(cityA, cityB))))
                }
            )
            val tested = tested()
            val statuses = mutableListOf<Event<SupportedCitiesStatus>>()
            tested.supportedCities.observeForever { statuses.add(it) }

            // when
            tested.forceFetchSupportedCities()

            // then
            verify(selectedCityUseCase).execute()
            verify(supportedCitiesUseCase).execute()
            Assert.assertEquals(1, statuses.size)
            Assert.assertEquals(true, statuses[0].content is SupportedCitiesStatus.Success)
            val second = statuses[0].content as SupportedCitiesStatus.Success
            val supportedCities = second.supportedCities
            Assert.assertEquals(2, supportedCities.size)
            Assert.assertEquals(true, supportedCities[0].currentlySelected)
            Assert.assertEquals(false, supportedCities[1].currentlySelected)
        }

    @Test
    fun `given observing supported cities and failed fetching supported cities when supported cities are being fetch then loading status followed by failure status is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(supportedCitiesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Error<SupportedCitiesData>("error"))
                }
            )
            val tested = tested()
            val statuses = mutableListOf<Event<SupportedCitiesStatus>>()
            tested.supportedCities.observeForever { statuses.add(it) }

            // when
            tested.forceFetchSupportedCities()

            // then
            verify(supportedCitiesUseCase).execute()
            Assert.assertEquals(1, statuses.size)
            Assert.assertEquals(true, statuses[0].content is SupportedCitiesStatus.Error)
        }

    @Test
    fun `given observing progress when currently selected city is fetched then progress visibility changes`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedCityUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(cityA))
                }
            )
            val tested = tested()
            val events = mutableListOf<Boolean>()
            tested.processing.observeForever {
                events.add(it)
            }

            // when
            events.clear()
            tested.fetchCurrentlySelectedCity()

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0])
            Assert.assertEquals(false, events[1])
        }

    @Test
    fun `given observing progress when supported cities are fetched then progress visibility changes`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(supportedCitiesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(SupportedCitiesData(listOf(cityA, cityB))))
                }
            )
            val tested = tested()
            val events = mutableListOf<Boolean>()
            tested.processing.observeForever { events.add(it) }

            // when
            events.clear()
            tested.forceFetchSupportedCities()

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0])
            Assert.assertEquals(false, events[1])
        }

    @Test
    fun `given observing selected city and new city is selected successfully when city is selected then city selected event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(citySelectionUseCase.execute(cityA)).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(cityA))
                }
            )

            val tested = tested()
            val events = mutableListOf<Event<CitySelection>>()
            tested.currentlySelectedCity.observeForever { events.add(it) }

            // when
            events.clear()
            tested.selectCity(cityA)

            // then
            verify(citySelectionUseCase).execute(cityA)
            Assert.assertEquals(1, events.size)
            Assert.assertEquals(true, events[0].content is CitySelection.Selected)
        }

    @Test
    fun `given observing selected city and error occurs for city selection when city is selected then city selected event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(citySelectionUseCase.execute(cityA)).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Error<CityPlan>("error"))
                }
            )

            val tested = tested()
            val events = mutableListOf<Event<CitySelection>>()
            tested.currentlySelectedCity.observeForever { events.add(it) }

            // when
            events.clear()
            tested.selectCity(cityA)

            // then
            verify(citySelectionUseCase).execute(cityA)
            Assert.assertEquals(1, events.size)
            Assert.assertEquals(true, events[0].content is CitySelection.NotSelected)
        }

    @Test
    fun `given observing progress when city is selected then progress visibility changes`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(citySelectionUseCase.execute(cityA)).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(cityA))
                }
            )
            val tested = tested()
            val events = mutableListOf<Boolean>()
            tested.processing.observeForever { events.add(it) }

            // when
            events.clear()
            tested.selectCity(cityA)

            // then
            Assert.assertEquals(2, events.size)
            Assert.assertEquals(true, events[0])
            Assert.assertEquals(false, events[1])
        }

    fun tested(
        supportedCitiesUseCase: SupportedCitiesUseCase = this.supportedCitiesUseCase,
        citySelectionUseCase: CitySelectionUseCase = this.citySelectionUseCase,
        selectedCityUseCase: SelectedCityUseCase = this.selectedCityUseCase,
        ioDispatcher: CoroutineDispatcher = coroutinesTestRule.testDispatcher
    ) = CityPickViewModelImpl(
        supportedCitiesUseCase,
        citySelectionUseCase,
        selectedCityUseCase,
        ioDispatcher
    )
}