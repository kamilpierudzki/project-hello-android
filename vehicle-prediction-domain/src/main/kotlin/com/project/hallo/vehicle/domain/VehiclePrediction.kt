package com.project.hallo.vehicle.domain

import com.project.hallo.city.plan.domain.model.Line

interface VehiclePrediction {
    fun processInput(rawInput: String, cityLines: List<Line>): List<Line>
}