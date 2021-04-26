package com.project.hallo.city.plan.domain.datasource

import com.project.hallo.city.plan.domain.model.api.CityPlanAPI
import com.project.hallo.commons.domain.repository.Response

interface CityDataSource {
    fun fetchCityData(city: String): Response<CityPlanAPI>
}