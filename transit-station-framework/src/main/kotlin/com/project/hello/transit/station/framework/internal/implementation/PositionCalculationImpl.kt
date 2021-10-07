package com.project.hello.transit.station.framework.internal.implementation

import com.project.hello.transit.station.framework.internal.PositionCalculation
import com.project.hello.transit.station.framework.internal.Result
import android.location.Location.distanceBetween
import com.project.hello.transit.station.framework.internal.Location
import javax.inject.Inject

internal class PositionCalculationImpl @Inject constructor(): PositionCalculation {

    override fun findTheClosestResultOrNull(location: Location, results: List<Result>): Result? =
        results.map {
            val endLatitude = it.geometry.location.lat
            val endLongitude = it.geometry.location.lng
            val calculationResults = FloatArray(1)
            distanceBetween(
                location.lat,
                location.lng,
                endLatitude,
                endLongitude,
                calculationResults
            )
            it to calculationResults[0]
        }
            .toMap()
            .entries
            .sortedBy {
                it.value
            }
            .map {
                it.key
            }
            .firstOrNull()
}