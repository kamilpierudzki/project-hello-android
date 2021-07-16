package com.project.hallo.city.plan.domain.repository.implementation

import com.project.hallo.city.plan.domain.datasource.CityDataSource
import com.project.hallo.city.plan.domain.datasource.SelectedCityDataSource
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.api.City
import com.project.hallo.city.plan.domain.model.api.CityPlanAPI
import com.project.hallo.city.plan.domain.repository.CityPlanRepository
import com.project.hallo.city.plan.domain.repository.resource.CityDataResource
import com.project.hallo.commons.domain.repository.Response

class CityPlanRepositoryImpl(
    private val resourcesSource: CityDataSource,
    private val selectedCityDataSource: SelectedCityDataSource
) : CityPlanRepository {

    override fun getSupportedCityFileResources(): List<Int> = City.values().map { it.file }

    override fun getCityDataResource() = object : CityDataResource<CityPlan, CityPlanAPI> {
        override fun load(fileRes: Int): Response<CityPlanAPI> {
            return resourcesSource.fetchCityData(fileRes)
        }

        override fun getCurrentlySelectedCity(): Response<CityPlan> {
            return selectedCityDataSource.loadCity()
        }

        override fun saveCurrentlySelectedCity(item: CityPlan) {
            selectedCityDataSource.saveCity(item)
        }
    }
}