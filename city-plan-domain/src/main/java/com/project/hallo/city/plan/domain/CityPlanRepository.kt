package com.project.hallo.city.plan.domain

class CityPlanRepository(private val cityPlanDataSource: CityPlanDataSource) {
    fun getCityPlan(vehicleType: VehicleType) = cityPlanDataSource.fetchPlanFor(vehicleType)
}