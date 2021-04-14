package com.project.hallo.vehicle.prediction.framework.internal

import com.project.hallo.city.plan.domain.VehicleType

data class PredictionViewModelInitialData(
    val targetVehicleTypes: List<VehicleType>,
    val countryCharacters: Map<String, String>
)