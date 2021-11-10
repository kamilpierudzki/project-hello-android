package com.project.hello.vehicle.prediction.framework.internal.station.implementation

import com.project.hello.vehicle.prediction.framework.internal.station.NearbySearchResultFiltration
import com.project.hello.vehicle.prediction.framework.internal.station.NearbySearchValues.ParamValue
import com.project.hello.vehicle.prediction.framework.internal.station.Result
import javax.inject.Inject

internal class NearbySearchResultFiltrationImpl @Inject constructor() :
    NearbySearchResultFiltration {

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