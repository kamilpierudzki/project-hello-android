package com.project.hello.vehicle.prediction.framework.internal

import com.project.hello.city.plan.domain.VehicleType
import com.project.hello.city.plan.domain.model.CityPlan

data class PredictionViewModelInitialData(
    val targetVehicleTypes: List<VehicleType>,
    val countryCharacters: Map<String, String>,
    val selectedCity: CityPlan
)