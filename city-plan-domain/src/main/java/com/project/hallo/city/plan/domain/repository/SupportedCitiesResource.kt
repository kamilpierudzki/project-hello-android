package com.project.hallo.city.plan.domain.repository

import com.project.hallo.commons.domain.repository.Response

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
interface SupportedCitiesResource<ResultType, RequestType> {
    fun saveFetchResult(item: RequestType)
    fun fetch(): Response<RequestType>
    fun loadFromDb(): Response<ResultType>
}