package com.project.hallo.city.plan.domain.repository

import com.project.hallo.city.plan.domain.datasource.SupportedCityDataSource
import com.project.hallo.city.plan.domain.model.SupportedCitiesData
import com.project.hallo.city.plan.domain.model.api.SupportedCitiesApi
import com.project.hallo.city.plan.domain.repository.resource.SupportedCitiesResource
import com.project.hallo.commons.domain.repository.Response

class SupportedCitiesRepository(
    private val resourcesSource: SupportedCityDataSource
) {

    fun getSupportedCitiesResource() =
        object : SupportedCitiesResource<SupportedCitiesData, SupportedCitiesApi> {

            override fun fetch(): Response<SupportedCitiesApi> {
                return resourcesSource.fetchSupportedCities()
            }

            override fun saveFetchResult(item: SupportedCitiesApi) {
                // todo implement
            }

            override fun loadFromDb(): Response<SupportedCitiesData> {
                return Response.Error("no data in DB")
            }
        }
}