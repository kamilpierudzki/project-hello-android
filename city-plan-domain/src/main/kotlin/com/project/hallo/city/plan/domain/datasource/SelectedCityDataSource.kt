package com.project.hallo.city.plan.domain.datasource

import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.commons.domain.repository.Response

interface SelectedCityDataSource {
    fun saveCity(city: CityPlan)
    fun loadCity(): Response<CityPlan>
}