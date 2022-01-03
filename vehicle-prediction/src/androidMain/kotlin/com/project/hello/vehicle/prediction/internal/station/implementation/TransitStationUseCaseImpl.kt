package com.project.hello.vehicle.prediction.internal.station.implementation

import android.location.Location
import com.project.hello.commons.hilt.IoDispatcher
import com.project.hello.vehicle.prediction.internal.station.*
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

internal typealias InternalLocation = com.project.hello.vehicle.prediction.internal.station.model.Location