package com.project.hello.vehicle.prediction.internal.station

import com.project.hello.vehicle.prediction.internal.station.model.ResultAPI

internal interface NearbySearchResultFiltration {
    fun filteredTransitStations(results: List<ResultAPI>): List<ResultAPI>
}