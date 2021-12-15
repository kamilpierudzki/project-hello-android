package com.project.hello.vehicle.prediction.internal.station

internal interface PositionCalculation {
    fun findTheClosestResultOrNull(location: Location, results: List<Result>): Result?
}