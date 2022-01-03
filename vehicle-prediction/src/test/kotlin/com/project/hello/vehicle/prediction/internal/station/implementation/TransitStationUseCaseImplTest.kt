package com.project.hello.vehicle.prediction.internal.station.implementation

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.hello.commons.test.CoroutinesTestRule
import com.project.hello.transit.agency.model.TransitAgency
import com.project.hello.vehicle.prediction.internal.station.*
import com.project.hello.vehicle.prediction.internal.station.model.GeometryAPI
import com.project.hello.vehicle.prediction.internal.station.model.LocationAPI
import com.project.hello.vehicle.prediction.internal.station.model.NearbySearchAPI
import com.project.hello.vehicle.prediction.internal.station.model.ResultAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
internal class TransitStationUseCaseImplTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    val nearbySearchUseCase: NearbySearchUseCase = mock()
    val nearbySearchResultFiltration: NearbySearchResultFiltration = mock()
    val positionCalculation: PositionCalculation = mock()
    val transitStationFiltration: TransitStationFiltration = mock()

    val data = TransitStationData(
        Location(""),
        TransitAgency(
            "", "", emptyList(), emptyList(),
            emptyList(), emptyList()
        )
    )

    val tested = TransitStationUseCaseImpl(
        coroutinesTestRule.testDispatcher,
        nearbySearchUseCase,
        nearbySearchResultFiltration,
        positionCalculation,
        transitStationFiltration
    )

    @Test
    fun `given successful api result when execute is called then result is processed`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(nearbySearchUseCase.getNearbySearchResult(any()))
                .thenReturn(NearbySearchAPI(emptyList(), ""))

            // when
            tested.execute(data).collect {}

            // then
            verify(nearbySearchResultFiltration).filteredTransitStations(any())
            verify(positionCalculation).findTheClosestResultOrNull(any(), any())
        }

    @Test
    fun `given the closest result is NOT null when execute is called then transit stations are filtered`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(nearbySearchUseCase.getNearbySearchResult(any()))
                .thenReturn(NearbySearchAPI(emptyList(), ""))

            val result = ResultAPI("", "", emptyList(), GeometryAPI(LocationAPI(0.0, 0.0)))
            whenever(positionCalculation.findTheClosestResultOrNull(any(), any()))
                .thenReturn(result)

            val filtrationResult = FiltrationResult(emptyList(), emptyList())
            whenever(transitStationFiltration.filteredStationsWithName(any(), any()))
                .thenReturn(filtrationResult)

            // when
            tested.execute(data).collect {}

            // then
            verify(transitStationFiltration).filteredStationsWithName(any(), any())
        }

    @Test
    fun `given the closest result is null when execute is called then transit stations are filtered`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(nearbySearchUseCase.getNearbySearchResult(any()))
                .thenReturn(NearbySearchAPI(emptyList(), ""))

            whenever(positionCalculation.findTheClosestResultOrNull(any(), any()))
                .thenReturn(null)

            // when
            tested.execute(data).collect {}

            // then
            verify(transitStationFiltration, never()).filteredStationsWithName(any(), any())
        }

    @Test
    fun `given the closest result is NOT null, the closest result was found when execute is called then event is emitted`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            whenever(nearbySearchUseCase.getNearbySearchResult(any()))
                .thenReturn(NearbySearchAPI(emptyList(), ""))

            val result = ResultAPI("", "", emptyList(), GeometryAPI(LocationAPI(0.0, 0.0)))
            whenever(positionCalculation.findTheClosestResultOrNull(any(), any()))
                .thenReturn(result)

            val filtrationResult = FiltrationResult(emptyList(), emptyList())
            whenever(transitStationFiltration.filteredStationsWithName(any(), any()))
                .thenReturn(filtrationResult)

            // when
            val events = mutableListOf<TransitStationResult>()
            tested.execute(data).collect { events.add(it) }

            // then
            Assert.assertEquals(1, events.size)
        }
}