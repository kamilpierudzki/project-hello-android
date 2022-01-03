package com.project.hello.vehicle.prediction.internal.station

import com.project.hello.vehicle.prediction.internal.station.model.Location
import com.project.hello.vehicle.prediction.internal.station.model.Result

internal interface PositionCalculation {
    fun findTheClosestResultOrNull(location: Location, results: List<Result>): Result?
}