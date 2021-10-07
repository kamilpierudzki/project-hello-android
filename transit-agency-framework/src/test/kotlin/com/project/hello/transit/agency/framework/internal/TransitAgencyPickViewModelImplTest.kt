package com.project.hello.transit.agency.framework.internal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.hello.transit.agency.domain.model.SupportedTransitAgenciesData
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.framework.api.SupportedTransitAgenciesStatus
import com.project.hello.transit.agency.framework.api.TransitAgencySelection
import com.project.hello.transit.agency.framework.internal.usecase.SelectedTransitAgencyUseCase
import com.project.hello.transit.agency.framework.internal.usecase.SupportedTransitAgenciesUseCase
import com.project.hello.transit.agency.framework.internal.usecase.TransitAgencySelectionUseCase
import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.domain.test.CoroutinesTestRule
import com.project.hello.commons.framework.livedata.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@InternalCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
internal class TransitAgencyPickViewModelImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    val cityA = createTransitAgency("A")
    val cityB = createTransitAgency("B")

    val supportedTransitAgenciesUseCase: SupportedTransitAgenciesUseCase = mock {
        on { execute() } doReturn flow {
            emit(Response.Loading<SupportedTransitAgenciesData>())
        }
    }
    val transitAgencySelectionUseCase: TransitAgencySelectionUseCase = mock {
        on { execute(any()) } doReturn flow {
            emit(Response.Loading<TransitAgency>())
        }
    }
    val selectedTransitAgencyUseCase: SelectedTransitAgencyUseCase = mock {
        on { execute() } doReturn flow {
            emit(Response.Loading<TransitAgency>())
        }
    }

    @Test
    fun `given city is selected when view model is initialised then SelectedCityUseCase is executed`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedTransitAgencyUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Success(cityA))
                }
            )

            // when
            tested()

            // then
            verify(selectedTransitAgencyUseCase).execute()
        }

    @Test
    fun `given city is selected when view model is initialised then SupportedCitiesUseCase is executed`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedTransitAgencyUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Success(cityA))
                }
            )

            // when
            tested()

            // then
            verify(supportedTransitAgenciesUseCase).execute()
        }

    @Test
    fun `given city is selected when view model is initialised then successful event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedTransitAgencyUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Success(cityA))
                    emit(Response.Success(cityB))
                }
            )

            // when
            val tested = tested(selectedTransitAgencyUseCase = selectedTransitAgencyUseCase)
            lateinit var selection1: TransitAgencySelection.Selected
            lateinit var selection2: TransitAgencySelection.Selected
            tested.currentlySelectedTransitAgencyEvent.observeForever {
                selection1 = it.consumeAndReturn() as TransitAgencySelection.Selected
            }
            tested.currentlySelectedTransitAgencyChangedEvent.observeForever {
                selection2 = it.consumeAndReturn() as TransitAgencySelection.Selected
            }

            // then
            verify(selectedTransitAgencyUseCase).execute()
            Assert.assertEquals("B", selection1.transitAgency.transitAgency)
            Assert.assertEquals("B", selection2.transitAgency.transitAgency)
        }

    @Test
    fun `given city is not selected when view model is initialised then failure event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedTransitAgencyUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Error<TransitAgency>("empty"))
                }
            )

            // when
            val tested = tested(selectedTransitAgencyUseCase = selectedTransitAgencyUseCase)
            lateinit var selection1: TransitAgencySelection
            lateinit var selection2: TransitAgencySelection
            tested.currentlySelectedTransitAgencyEvent.observeForever { selection1 = it.content }
            tested.currentlySelectedTransitAgencyChangedEvent.observeForever { selection2 = it.content }

            // then
            verify(selectedTransitAgencyUseCase).execute()
            Assert.assertEquals(true, selection1 is TransitAgencySelection.NotSelected)
            Assert.assertEquals(true, selection2 is TransitAgencySelection.NotSelected)
        }

    @Test
    fun `given observing supported cities and successful fetching supported cities when view model is initialised then loading status followed by successful status is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(supportedTransitAgenciesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(SupportedTransitAgenciesData(listOf(cityA, cityB))))
                }
            )

            // when
            val tested = tested()
            val statuses = mutableListOf<Event<SupportedTransitAgenciesStatus>>()
            tested.supportedTransitAgencies.observeForever { statuses.add(it) }

            // then
            verify(supportedTransitAgenciesUseCase).execute()
            Assert.assertEquals(1, statuses.size)
            Assert.assertEquals(true, statuses[0].content is SupportedTransitAgenciesStatus.Success)
            val second = statuses[0].content as SupportedTransitAgenciesStatus.Success
            Assert.assertEquals(2, second.supportedTransitAgencies.size)
        }

    @Test
    fun `given observing supported cities, successful fetching supported cities and currently selected city is available when view model is initialised then loading status followed by successful status is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedTransitAgencyUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Success(cityA))
                }
            )
            whenever(supportedTransitAgenciesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(SupportedTransitAgenciesData(listOf(cityA, cityB))))
                }
            )

            // when
            val tested = tested()
            val statuses = mutableListOf<Event<SupportedTransitAgenciesStatus>>()
            tested.supportedTransitAgencies.observeForever { statuses.add(it) }

            // then
            verify(selectedTransitAgencyUseCase).execute()
            verify(supportedTransitAgenciesUseCase).execute()
            Assert.assertEquals(1, statuses.size)
            Assert.assertEquals(true, statuses[0].content is SupportedTransitAgenciesStatus.Success)
            val second = statuses[0].content as SupportedTransitAgenciesStatus.Success
            val supportedCities = second.supportedTransitAgencies
            Assert.assertEquals(2, supportedCities.size)
        }

    @Test
    fun `given observing supported cities and failed fetching supported cities when view model is initialised then loading status followed by failure status is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(supportedTransitAgenciesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Error<SupportedTransitAgenciesData>("error"))
                }
            )

            // when
            val tested = tested()
            val statuses = mutableListOf<Event<SupportedTransitAgenciesStatus>>()
            tested.supportedTransitAgencies.observeForever { statuses.add(it) }

            // then
            verify(supportedTransitAgenciesUseCase).execute()
            Assert.assertEquals(1, statuses.size)
            Assert.assertEquals(true, statuses[0].content is SupportedTransitAgenciesStatus.Error)
        }

    @Test
    fun `given selected city is available and observing progress when view model is initialised then progress visibility changes`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(selectedTransitAgencyUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(cityA))
                }
            )
            whenever(supportedTransitAgenciesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(SupportedTransitAgenciesData(listOf(cityA, cityB))))
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
            whenever(selectedTransitAgencyUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(cityA))
                }
            )
            whenever(supportedTransitAgenciesUseCase.execute()).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(SupportedTransitAgenciesData(listOf(cityA, cityB))))
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
            whenever(transitAgencySelectionUseCase.execute(cityA)).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Success(cityA))
                }
            )

            val tested = tested()
            val events1 = mutableListOf<Event<TransitAgencySelection>>()
            tested.currentlySelectedTransitAgencyEvent.observeForever { events1.add(it) }
            val events2 = mutableListOf<Event<TransitAgencySelection>>()
            tested.currentlySelectedTransitAgencyChangedEvent.observeForever { events2.add(it) }

            // when
            events1.clear()
            events2.clear()
            tested.selectTransitAgency(cityA)

            // then
            verify(transitAgencySelectionUseCase).execute(cityA)
            Assert.assertEquals(1, events1.size)
            Assert.assertEquals(1, events2.size)
            Assert.assertEquals(true, events1[0].content is TransitAgencySelection.Selected)
            Assert.assertEquals(true, events2[0].content is TransitAgencySelection.Selected)
        }

    @Test
    fun `given observing selected city and error occurs for city selection when city is selected then city selected event is sent`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(transitAgencySelectionUseCase.execute(cityA)).thenReturn(
                flow {
                    emit(Response.Loading())
                    emit(Response.Error<TransitAgency>("error"))
                }
            )

            val tested = tested()
            val events1 = mutableListOf<Event<TransitAgencySelection>>()
            tested.currentlySelectedTransitAgencyEvent.observeForever { events1.add(it) }
            val events2 = mutableListOf<Event<TransitAgencySelection>>()
            tested.currentlySelectedTransitAgencyChangedEvent.observeForever { events2.add(it) }

            // when
            events1.clear()
            events2.clear()
            tested.selectTransitAgency(cityA)

            // then
            verify(transitAgencySelectionUseCase).execute(cityA)
            Assert.assertEquals(1, events1.size)
            Assert.assertEquals(1, events2.size)
            Assert.assertEquals(true, events1[0].content is TransitAgencySelection.NotSelected)
            Assert.assertEquals(true, events2[0].content is TransitAgencySelection.NotSelected)
        }

    @Test
    fun `given observing progress when city is selected then progress visibility changes`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(transitAgencySelectionUseCase.execute(cityA)).thenReturn(
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
            tested.selectTransitAgency(cityA)

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
            tested.currentlySelectedTransitAgencyEvent.postValue(Event(TransitAgencySelection.Selected(cityA)))

            // then
            val currentlySelectedCity = tested.currentlySelectedTransitAgency
            Assert.assertEquals(true, currentlySelectedCity != null)
            Assert.assertEquals(cityA, currentlySelectedCity)
        }

    fun tested(
        supportedTransitAgenciesUseCase: SupportedTransitAgenciesUseCase = this.supportedTransitAgenciesUseCase,
        transitAgencySelectionUseCase: TransitAgencySelectionUseCase = this.transitAgencySelectionUseCase,
        selectedTransitAgencyUseCase: SelectedTransitAgencyUseCase = this.selectedTransitAgencyUseCase,
        ioDispatcher: CoroutineDispatcher = coroutinesTestRule.testDispatcher
    ) = TransitAgencyPickViewModelImpl(
        supportedTransitAgenciesUseCase,
        transitAgencySelectionUseCase,
        selectedTransitAgencyUseCase,
        ioDispatcher
    )

    private fun createTransitAgency(transitAgency: String) = TransitAgency(
        transitAgency = transitAgency,
        lastUpdateFormatted = "",
        tramLines = emptyList(),
        busLines = emptyList(),
        tramStops = emptyList(),
        busStops = emptyList(),
    )
}