package com.project.hello.vehicle.prediction.internal.station

import com.project.hello.vehicle.prediction.internal.station.model.LocationAPI
import com.project.hello.vehicle.prediction.internal.station.model.ResultAPI

internal interface PositionCalculation {
    fun findTheClosestResultOrNull(locationAPI: LocationAPI, results: List<ResultAPI>): ResultAPI?
}