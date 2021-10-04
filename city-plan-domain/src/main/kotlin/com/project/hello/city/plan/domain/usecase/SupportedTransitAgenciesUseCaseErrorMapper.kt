package com.project.hello.city.plan.domain.usecase

import com.project.hello.city.plan.domain.model.SupportedTransitAgenciesData
import com.project.hello.commons.domain.data.Response

interface SupportedTransitAgenciesUseCaseErrorMapper {
    fun mapError(error: Response.Error<SupportedTransitAgenciesData>)
}