package com.project.hello.vehicle.domain

import com.project.hello.city.plan.domain.model.Line

interface VehiclePrediction {
    fun mostProbableLine(rawInput: String, cityLines: List<Line>): Line?
}