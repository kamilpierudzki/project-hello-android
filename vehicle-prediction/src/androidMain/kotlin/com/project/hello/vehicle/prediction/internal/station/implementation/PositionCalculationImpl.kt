package com.project.hello.vehicle.prediction.internal.station.implementation

import android.location.Location.distanceBetween
import com.project.hello.vehicle.prediction.internal.station.PositionCalculation
import com.project.hello.vehicle.prediction.internal.station.model.LocationAPI
import com.project.hello.vehicle.prediction.internal.station.model.ResultAPI
import javax.inject.Inject

internal class PositionCalculationImpl @Inject constructor() : PositionCalculation {

    override fun findTheClosestResultOrNull(locationAPI: LocationAPI, results: List<ResultAPI>): ResultAPI? =
        results.map {
            val endLatitude = it.geometryAPI.location.lat
            val endLongitude = it.geometryAPI.location.lng
            val calculationResults = FloatArray(1)
            distanceBetween(
                locationAPI.lat,
                locationAPI.lng,
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