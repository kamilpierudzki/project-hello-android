package com.project.hello.transit.station.framework.internal

import com.project.hello.transit.agency.domain.model.Stop
import com.project.hello.transit.agency.domain.model.TransitAgency

interface TransitStationFiltration {
    fun filteredStationsWithName(
        stationName: String,
        transitAgency: TransitAgency
    ): FiltrationResult
}

data class FiltrationResult(
    val filteredTramStops: List<Stop>,
    val filteredBusStops: List<Stop>,
)