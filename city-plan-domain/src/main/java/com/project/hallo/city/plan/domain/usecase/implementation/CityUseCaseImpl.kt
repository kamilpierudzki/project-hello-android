package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.api.CityPlanAPI
import com.project.hallo.city.plan.domain.model.api.toCityPlan
import com.project.hallo.city.plan.domain.repository.CityPlanRepository
import com.project.hallo.city.plan.domain.usecase.CityUseCase
import com.project.hallo.commons.domain.repository.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CityUseCaseImpl(
    private val cityPlanRepository: CityPlanRepository
) : CityUseCase {

    override fun execute(city: String): Flow<Response<CityPlan>> = flow {
        emit(Response.Loading())
        val resource = cityPlanRepository.getCityDataResource(city)
        when (val apiResponse: Response<CityPlanAPI> = resource.fetch(city)) {
            is Response.Error -> {
                emit(Response.Error<CityPlan>("Couldn't fetch data for selected city"))
            }
            is Response.Success -> {
                val apiData: CityPlanAPI = apiResponse.data!!
                resource.saveFetchResult(apiData)
                val data = Response.Success(apiData.toCityPlan())
                emit(data)
            }
        }
    }
}