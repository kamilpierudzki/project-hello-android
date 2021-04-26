package com.project.hallo.city.plan.domain.repository.resource

import com.project.hallo.commons.domain.repository.Response

interface SupportedCitiesResource<ResultType, RequestType> {
    fun saveFetchResult(item: RequestType)
    fun fetch(): Response<RequestType>
    fun loadFromDb(): Response<ResultType>
}