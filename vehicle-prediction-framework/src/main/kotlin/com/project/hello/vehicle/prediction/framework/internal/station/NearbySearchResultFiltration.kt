package com.project.hello.vehicle.prediction.framework.internal.station

internal interface NearbySearchResultFiltration {
    fun filteredTransitStations(results: List<Result>): List<Result>
}