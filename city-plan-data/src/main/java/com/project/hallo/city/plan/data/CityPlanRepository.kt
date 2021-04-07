package com.project.hallo.city.plan.data

import com.project.hallo.city.plan.domain.VehicleType

class CityPlanRepository(private val cityPlanDataSource: CityPlanDataSource) {
    fun getCityPlan(vehicleType: VehicleType) = cityPlanDataSource.fetchPlanFor(vehicleType)
}