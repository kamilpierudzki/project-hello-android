package com.project.hallo.city.plan.domain.datasource

import com.project.hallo.city.plan.domain.model.api.SupportedCitiesApi
import com.project.hallo.commons.domain.repository.ApiResponse

interface SupportedCityDataSource {
    fun fetchSupportedCities(): ApiResponse<SupportedCitiesApi>
}