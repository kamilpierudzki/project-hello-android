package com.project.hello.vehicle.prediction.framework.internal.station.implementation

import com.project.hello.transit.agency.domain.model.Stop
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.vehicle.prediction.framework.internal.station.FiltrationResult
import com.project.hello.vehicle.prediction.framework.internal.station.TransitStationFiltration
import javax.inject.Inject

internal class TransitStationFiltrationImpl @Inject constructor() : TransitStationFiltration {

    override fun filteredStationsWithName(
        stationName: String,
        transitAgency: TransitAgency
    ): FiltrationResult {
        val filteredTramStops = filter(stationName, transitAgency.tramStops)
        val filteredBusStops = filter(stationName, transitAgency.busStops)
        return FiltrationResult(
            filteredTramStops = filteredTramStops,
            filteredBusStops = filteredBusStops,
        )
    }

    private fun filter(stationName: String, stops: List<Stop>): List<Stop> =
        stops
            .asSequence()
            .filter {
                stationName == it.stopName
            }
            .toList()
}