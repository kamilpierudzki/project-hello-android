package com.project.hello.transit.agency.domain.usecase

import com.project.hello.commons.domain.data.Response
import com.project.hello.transit.agency.domain.model.SupportedTransitAgenciesData

interface SupportedTransitAgenciesUseCaseErrorMapper {
    fun mapError(error: Response.Error<SupportedTransitAgenciesData>)
}