package com.project.hallo.city.plan.domain.repository.resource

import com.project.hallo.commons.domain.repository.Response

interface SelectedCityResource<ResultType> {
    fun loadFromDb(): Response<ResultType>
}