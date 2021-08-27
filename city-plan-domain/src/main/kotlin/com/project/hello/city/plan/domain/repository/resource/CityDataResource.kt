package com.project.hello.city.plan.domain.repository.resource

import com.project.hello.commons.domain.data.ResponseApi

interface CityDataResource<ResultType, RequestType> {
    fun saveCurrentlySelectedCity(item: ResultType)
    fun load(fileRes: Int): ResponseApi<RequestType>
    fun getCurrentlySelectedCity(): ResponseApi<ResultType>
}