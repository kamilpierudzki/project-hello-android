package com.project.hallo.city.plan.domain.datasource

import com.project.hallo.city.plan.domain.CityPlanAPI

interface CityPlanDataSource {
    fun fetchPlanFor(): CityPlanAPI
}