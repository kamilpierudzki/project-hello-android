package com.project.hallo.city.plan.domain.repository.resource

import com.project.hallo.commons.domain.repository.Response

interface CityDataResource<ResultType, RequestType> {
    fun saveCurrentlySelectedCity(item: ResultType)
    fun load(fileRes: Int): Response<RequestType>
    fun getCurrentlySelectedCity(): Response<ResultType>
}