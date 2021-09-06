package com.project.hello.vehicle.domain

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.LineWithAccuracy

interface VehiclePrediction {
    fun processInput(rawInput: String, cityLines: List<Line>): List<LineWithAccuracy>
}