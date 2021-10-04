package com.project.hello.vehicle.prediction.framework.internal

import com.project.hello.transit.agency.domain.VehicleType
import com.project.hello.transit.agency.domain.model.TransitAgency

data class PredictionViewModelInitialData(
    val targetVehicleTypes: List<VehicleType>,
    val countryCharacters: Map<String, String>,
    val selectedCity: TransitAgency
)