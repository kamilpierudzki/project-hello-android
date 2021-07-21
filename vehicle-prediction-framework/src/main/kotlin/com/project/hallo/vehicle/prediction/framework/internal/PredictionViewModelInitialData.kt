package com.project.hallo.vehicle.prediction.framework.internal

import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.domain.model.CityPlan

data class PredictionViewModelInitialData(
    val targetVehicleTypes: List<VehicleType>,
    val countryCharacters: Map<String, String>,
    val selectedCity: CityPlan
)