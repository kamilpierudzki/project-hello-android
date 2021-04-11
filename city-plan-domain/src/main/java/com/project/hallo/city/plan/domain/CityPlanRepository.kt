package com.project.hallo.city.plan.domain

class CityPlanRepository(private val localCityPlanDataSource: CityPlanDataSource) {

    fun getCityPlan(vehicleType: VehicleType): List<Line> {
        val cityPlanApi = localCityPlanDataSource.fetchPlanFor(vehicleType)
        return cityPlanApi.trams?.map {
            Line.fromLineAPI(it)
        } ?: emptyList()
    }
}