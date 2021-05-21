package com.project.hallo.city.plan.domain.repository.resource

import com.project.hallo.commons.domain.repository.Response

interface SupportedCitiesResource<RequestType> {
    fun fetch(resFile: Int): Response<RequestType>
}