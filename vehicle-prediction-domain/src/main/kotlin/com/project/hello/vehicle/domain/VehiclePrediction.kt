package com.project.hello.vehicle.domain

import com.project.hello.transit.agency.domain.model.Line
import com.project.hello.vehicle.domain.timeout.TimeoutChecker

interface VehiclePrediction {
    fun predictLine(rawInput: String, cityLines: List<Line>, timeoutChecker: TimeoutChecker): Line?
}