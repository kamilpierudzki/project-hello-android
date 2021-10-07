package com.project.hello.transit.station.framework.internal

internal interface PositionCalculation {
    fun findTheClosestResultOrNull(location: Location, results: List<Result>): Result?
}