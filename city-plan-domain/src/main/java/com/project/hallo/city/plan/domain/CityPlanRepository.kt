package com.project.hallo.city.plan.domain

import com.project.hallo.city.plan.domain.datasource.CityPlanDataSource

class CityPlanRepository(private val localCityPlanDataSource: CityPlanDataSource) {

    fun getCityPlan(targetTypes: List<VehicleType>): List<Line> {
        val cityPlanApi = localCityPlanDataSource.fetchPlanFor()
        return targetTypes
            .map {
                when (it) {
                    VehicleType.BUS -> cityPlanApi.buses
                    VehicleType.TRAM -> cityPlanApi.trams
                }
            }
            .flatMap { it.toList() }
            .map { Line.fromLineAPI(it) }
    }
}