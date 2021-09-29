package com.project.hello.vehicle.domain

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.TimeoutChecker

interface VehiclePrediction {
    fun predictLine(rawInput: String, cityLines: List<Line>, timeoutChecker: TimeoutChecker): Line?
}