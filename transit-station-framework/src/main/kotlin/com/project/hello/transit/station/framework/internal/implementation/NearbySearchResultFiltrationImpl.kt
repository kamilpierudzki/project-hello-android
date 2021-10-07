package com.project.hello.transit.station.framework.internal.implementation

import com.project.hello.transit.station.framework.internal.NearbySearchValues.ParamValue
import com.project.hello.transit.station.framework.internal.Result
import com.project.hello.transit.station.framework.internal.NearbySearchResultFiltration
import javax.inject.Inject

internal class NearbySearchResultFiltrationImpl @Inject constructor() : NearbySearchResultFiltration {

    override fun filteredTransitStations(results: List<Result>): List<Result> =
        results
            .asSequence()
            .filter {
                containsMatchingType(it.types)
            }
            .toList()

    private fun containsMatchingType(types: List<String>): Boolean =
        types.any { it == ParamValue.PLACE_TYPE_VALUE }
}