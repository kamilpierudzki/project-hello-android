package com.project.hallo.vehicle.prediction.framework.internal

import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.framework.internal.datamodel.CityPlanParcelable

data class PredictionViewModelInitialData(
    val targetVehicleTypes: List<VehicleType>,
    val countryCharacters: Map<String, String>,
    val selectedCityParcelable: CityPlanParcelable
)