package com.project.hello.vehicle.prediction.framework.internal.station

import com.project.hello.transit.agency.domain.model.Stop
import com.project.hello.transit.agency.domain.model.TransitAgency

internal interface TransitStationFiltration {
    fun filteredStationsWithName(
        stationName: String,
        transitAgency: TransitAgency
    ): FiltrationResult
}

internal data class FiltrationResult(
    val filteredTramStops: List<Stop>,
    val filteredBusStops: List<Stop>,
)