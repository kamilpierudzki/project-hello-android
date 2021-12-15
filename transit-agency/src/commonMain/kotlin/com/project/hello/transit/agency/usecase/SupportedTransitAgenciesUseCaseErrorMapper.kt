package com.project.hello.transit.agency.usecase

import com.project.hello.commons.data.Response
import com.project.hello.transit.agency.model.SupportedTransitAgenciesData

interface SupportedTransitAgenciesUseCaseErrorMapper {
    fun mapError(error: Response.Error<SupportedTransitAgenciesData>)
}