package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.model.SupportedCitiesData
import com.project.hallo.city.plan.domain.model.api.SupportedCitiesApi
import com.project.hallo.city.plan.domain.model.api.toSupportedCitiesData
import com.project.hallo.city.plan.domain.repository.SupportedCitiesRepository
import com.project.hallo.city.plan.domain.usecase.SupportedCitiesUseCase
import com.project.hallo.commons.domain.repository.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SupportedCitiesUseCaseImpl(
    private val supportedCitiesRepository: SupportedCitiesRepository
) : SupportedCitiesUseCase {


    override fun getSupportedCities(): Flow<Response<SupportedCitiesData>> {
        return flow {
            emit(Response.Loading())
            val supportedCitiesResource = supportedCitiesRepository.getSupportedCitiesResource()
            when (val apiResponse: Response<SupportedCitiesApi> = supportedCitiesResource.fetch()) {
                is Response.Success -> {
                    val apiData = apiResponse.data!!
                    supportedCitiesResource.saveFetchResult(apiData)
                    val data = Response.Success(apiData.toSupportedCitiesData())
                    emit(data)
                }
                is Response.Error -> {
                    when (val dbResponse = supportedCitiesResource.loadFromDb()) {
                        is Response.Success -> {
                            emit(dbResponse)
                        }
                        is Response.Error -> {
                            emit(Response.Error<SupportedCitiesData>("Couldn't get supported cities"))
                        }
                    }
                }
            }
        }
    }
}