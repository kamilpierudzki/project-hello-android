package com.project.hallo.city.plan.domain

interface CityPlanDataSource {
    fun fetchPlanFor(vehicleType: VehicleType): CityPlanAPI
}