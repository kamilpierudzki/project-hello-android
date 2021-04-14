package com.project.hallo.vehicle.domain

import com.project.hallo.city.plan.domain.Line

interface VehiclePrediction {
    fun processInput(rawInput: String, cityLines: List<Line>): List<Line>
}