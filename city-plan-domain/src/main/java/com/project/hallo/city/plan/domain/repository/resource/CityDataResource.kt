package com.project.hallo.city.plan.domain.repository.resource

import com.project.hallo.commons.domain.repository.Response

interface CityDataResource<ResultType, RequestType> {
    fun saveFetchResult(item: RequestType)
    fun fetch(city: String): Response<RequestType>
    fun loadFromDb(): Response<ResultType>
}