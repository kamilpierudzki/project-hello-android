package com.project.hallo.city.plan.domain.repository

import com.project.hallo.city.plan.domain.datasource.SupportedCityDataSource
import com.project.hallo.city.plan.domain.model.SupportedCitiesData
import com.project.hallo.city.plan.domain.model.api.SupportedCitiesApi
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.domain.repository.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SupportedCitiesRepository(
    private val resourcesSource: SupportedCityDataSource
) {

    fun getSupportedCitiesResource() =
        object : SupportedCitiesResource<SupportedCitiesData, SupportedCitiesApi> {

            override fun fetch(): Response<SupportedCitiesApi> {
                return resourcesSource.fetchSupportedCities()
            }

            override fun saveFetchResult(item: SupportedCitiesApi) {
            }

            override fun loadFromDb(): Response<SupportedCitiesData> {
                return Response.Success(SupportedCitiesData(listOf("poznań", "warszawa", "kraków")))
            }
        }
}