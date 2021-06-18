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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@FlowPreview
@ExperimentalCoroutinesApi
internal class CityPickViewModelImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    val cityA = CityPlan("A", emptyList(), emptyList())
    val cityB = CityPlan("B", emptyList(), emptyList())

    val supportedCitiesUseCase: SupportedCitiesUseCase = mock {
        on { execute() } doReturn flow {
            emit(Response.Loading<SupportedCitiesData>())
        }
    }
    val citySelectionUseCase: CitySelectionUseCase = mock {
        on { execute(any()) } doReturn flow {
            emit(Response.Loading<CityPlan>())
        }
    }
    val selectedCityUseCase: SelectedCityUseCase = mock {
        on { execute() } doReturn flow {
            emit(Response.Loading<CityPlan>())
        }
    }

    @Test
    fun `given city is selected when view model is initialised then SelectedCityUseCase is executed`() =
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
    fun `given city is selected when view model is initialised then SupportedCitiesUseCase is executed`() =
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
            verify(supportedCitiesUseCase).execute()
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
            lateinit var selection1: CitySelection.Selected
            lateinit var selection2: CitySelection.Selected
            tested.currentlySelectedCityEvent.observeForever {
                selection1 = it.getContentOrNull() as CitySelection.Selected
            }
            tested.currentlySelectedCityChangedEvent.observeForever {
                selection2 = it.getContentOrNull() as CitySelection.Selected
            }

            // then
            verify(selectedCityUseCase).execute()
            Assert.assertEquals("B", selection1.cityPlan.city)
            Assert.assertEquals("B", selection2.cityPlan.city)
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
            lateinit var selection1: CitySelection
            lateinit var selection2: CitySelection
            tested.currentlySelectedCityEvent.observeForever { selection1 = it.content }
            tested.currentlySelectedCityChangedEvent.observeForever { selection2 = it.content }

            // then
            verify(selectedCityUseCase).execute()
            Assert.assertEquals(true, selection1 is CitySelection.NotSelected)
            Assert.assertEquals(true, selection2 is CitySelection.NotSelected)
        }

    @Test
    fun `given observing supported cities and successful fetching supported cities when view model is initialised then loading status followed by successful status is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(supportedCitiesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(SupportedCitiesData(listOf(cityA, cityB))))
                }
            )

            // when
            val tested = tested()
            val statuses = mutableListOf<Event<SupportedCitiesStatus>>()
            tested.supportedCities.observeForever { statuses.add(it) }

            // then
            verify(supportedCitiesUseCase).execute()
            Assert.assertEquals(1, statuses.size)
            Assert.assertEquals(true, statuses[0].content is SupportedCitiesStatus.Success)
            val second = statuses[0].content as SupportedCitiesStatus.Success
            Assert.assertEquals(2, second.supportedCities.size)
        }

    @Test
    fun `given observing supported cities, successful fetching supported cities and currently selected city is available when view model is initialised then loading status followed by successful status is sent`() =
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

            // when
            val tested = tested()
            val statuses = mutableListOf<Event<SupportedCitiesStatus>>()
            tested.supportedCities.observeForever { statuses.add(it) }

            // then
            verify(selectedCityUseCase).execute()
            verify(supportedCitiesUseCase).execute()
            Assert.assertEquals(1, statuses.size)
            Assert.assertEquals(true, statuses[0].content is SupportedCitiesStatus.Success)
            val second = statuses[0].content as SupportedCitiesStatus.Success
            val supportedCities = second.supportedCities
            Assert.assertEquals(2, supportedCities.size)
        }

    @Test
    fun `given observing supported cities and failed fetching supported cities when view model is initialised then loading status followed by failure status is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(supportedCitiesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Error<SupportedCitiesData>("error"))
                }
            )

            // when
            val tested = tested()
            val statuses = mutableListOf<Event<SupportedCitiesStatus>>()
            tested.supportedCities.observeForever { statuses.add(it) }

            // then
            verify(supportedCitiesUseCase).execute()
            Assert.assertEquals(1, statuses.size)
            Assert.assertEquals(true, statuses[0].content is SupportedCitiesStatus.Error)
        }

    @Test
    fun `given selected city is available and observing progress when view model is initialised then progress visibility changes`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedCityUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
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
            val events = mutableListOf<Boolean>()
            tested.processing.observeForever { events.add(it) }

            // when
            events.clear()
            tested.fetchData()

            // then
            Assert.assertEquals(4, events.size)
            Assert.assertEquals(true, events[0])
            Assert.assertEquals(false, events[1])
            Assert.assertEquals(true, events[2])
            Assert.assertEquals(false, events[3])
        }

    @Test
    fun `given supported cities are available and observing progress when view model is initialised then progress visibility changes`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedCityUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
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
            val events = mutableListOf<Boolean>()
            tested.processing.observeForever { events.add(it) }

            // when
            events.clear()
            tested.fetchData()

            // then
            Assert.assertEquals(4, events.size)
            Assert.assertEquals(true, events[0])
            Assert.assertEquals(false, events[1])
            Assert.assertEquals(true, events[2])
            Assert.assertEquals(false, events[3])
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
            val events1 = mutableListOf<Event<CitySelection>>()
            tested.currentlySelectedCityEvent.observeForever { events1.add(it) }
            val events2 = mutableListOf<Event<CitySelection>>()
            tested.currentlySelectedCityChangedEvent.observeForever { events2.add(it) }

            // when
            events1.clear()
            events2.clear()
            tested.selectCity(cityA)

            // then
            verify(citySelectionUseCase).execute(cityA)
            Assert.assertEquals(1, events1.size)
            Assert.assertEquals(1, events2.size)
            Assert.assertEquals(true, events1[0].content is CitySelection.Selected)
            Assert.assertEquals(true, events2[0].content is CitySelection.Selected)
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
            val events1 = mutableListOf<Event<CitySelection>>()
            tested.currentlySelectedCityEvent.observeForever { events1.add(it) }
            val events2 = mutableListOf<Event<CitySelection>>()
            tested.currentlySelectedCityChangedEvent.observeForever { events2.add(it) }

            // when
            events1.clear()
            events2.clear()
            tested.selectCity(cityA)

            // then
            verify(citySelectionUseCase).execute(cityA)
            Assert.assertEquals(1, events1.size)
            Assert.assertEquals(1, events2.size)
            Assert.assertEquals(true, events1[0].content is CitySelection.NotSelected)
            Assert.assertEquals(true, events2[0].content is CitySelection.NotSelected)
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

    @Test
    fun `when currentlySelectedCityEvent changes currentlySelectedCity is available`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val tested = tested()

            // when
            tested.currentlySelectedCityEvent.postValue(Event(CitySelection.Selected(cityA)))

            // then
            val currentlySelectedCity = tested.currentlySelectedCity
            Assert.assertEquals(true, currentlySelectedCity != null)
            Assert.assertEquals(cityA, currentlySelectedCity)
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