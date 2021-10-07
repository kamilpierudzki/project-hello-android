package com.project.hello.transit.station.framework.internal.implementation

import android.location.Location
import com.project.hello.commons.framework.hilt.IoDispatcher
import com.project.hello.transit.station.framework.api.TransitStationData
import com.project.hello.transit.station.framework.api.TransitStationResult
import com.project.hello.transit.station.framework.api.TransitStationUseCase
import com.project.hello.transit.station.framework.internal.NearbySearchResultFiltration
import com.project.hello.transit.station.framework.internal.NearbySearchUseCase
import com.project.hello.transit.station.framework.internal.PositionCalculation
import com.project.hello.transit.station.framework.internal.TransitStationFiltration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class TransitStationUseCaseImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val nearbySearchUseCase: NearbySearchUseCase,
    private val nearbySearchResultFiltration: NearbySearchResultFiltration,
    private val positionCalculation: PositionCalculation,
    private val transitStationFiltration: TransitStationFiltration,
) : TransitStationUseCase {


    override fun execute(data: TransitStationData): Flow<TransitStationResult> = flow {
        val result = nearbySearchUseCase.getNearbySearchResult(data.location)
        if (result != null) {
            val transitStationResults = nearbySearchResultFiltration.filteredTransitStations(
                result.results
            )
            val theClosestResult = positionCalculation.findTheClosestResultOrNull(
                data.location.toInternalLocation(), transitStationResults
            )
            if (theClosestResult != null) {
                val transitStationName = theClosestResult.name
                val filtrationResult = transitStationFiltration.filteredStationsWithName(
                    transitStationName,
                    data.selectedTransitAgency
                )
                val transitStationResult = TransitStationResult(
                    tramStops = filtrationResult.filteredTramStops,
                    busStops = filtrationResult.filteredBusStops,
                )
                emit(transitStationResult)
            }
        }
    }
        .flowOn(ioDispatcher)

    private fun Location.toInternalLocation() = InternalLocation(latitude, longitude)
}

typealias InternalLocation = com.project.hello.transit.station.framework.internal.Location