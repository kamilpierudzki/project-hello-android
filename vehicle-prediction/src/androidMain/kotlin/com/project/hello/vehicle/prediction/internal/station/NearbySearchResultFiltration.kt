package com.project.hello.vehicle.prediction.internal.station

import com.project.hello.vehicle.prediction.internal.station.model.Result

internal interface NearbySearchResultFiltration {
    fun filteredTransitStations(results: List<Result>): List<Result>
}