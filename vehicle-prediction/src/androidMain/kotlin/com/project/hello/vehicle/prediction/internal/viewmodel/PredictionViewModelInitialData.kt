package com.project.hello.vehicle.prediction.internal.viewmodel

import com.project.hello.transit.agency.VehicleType
import com.project.hello.transit.agency.model.TransitAgency

data class PredictionViewModelInitialData(
    val targetVehicleTypes: List<VehicleType>,
    val countryCharacters: Map<String, String>,
    val selectedTransitAgency: TransitAgency
)