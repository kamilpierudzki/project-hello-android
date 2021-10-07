package com.project.hello.transit.station.framework.internal

internal interface NearbySearchResultFiltration {
    fun filteredTransitStations(results: List<Result>): List<Result>
}