package com.project.hello.vehicle.prediction.framework.internal.station.implementation

import android.location.Location.distanceBetween
import com.project.hello.vehicle.prediction.framework.internal.station.Location
import com.project.hello.vehicle.prediction.framework.internal.station.PositionCalculation
import com.project.hello.vehicle.prediction.framework.internal.station.Result
import javax.inject.Inject

internal class PositionCalculationImpl @Inject constructor() : PositionCalculation {

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