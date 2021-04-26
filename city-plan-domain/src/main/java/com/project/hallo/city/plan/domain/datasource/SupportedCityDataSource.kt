package com.project.hallo.city.plan.domain.datasource

import com.project.hallo.city.plan.domain.model.api.SupportedCitiesApi
import com.project.hallo.commons.domain.repository.Response

interface SupportedCityDataSource {
    fun fetchSupportedCities(): Response<SupportedCitiesApi>
}