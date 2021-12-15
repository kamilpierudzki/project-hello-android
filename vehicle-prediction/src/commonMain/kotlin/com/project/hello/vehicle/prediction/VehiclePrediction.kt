package com.project.hello.vehicle.prediction

import com.project.hello.transit.agency.model.Line
import com.project.hello.vehicle.prediction.timeout.TimeoutChecker

interface VehiclePrediction {
    fun predictLine(rawInput: String, cityLines: List<Line>, timeoutChecker: TimeoutChecker): Line?
}