package com.project.hello.transit.agency.domain.usecase

import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.commons.domain.data.Response

interface SelectedTransitAgencyUseCaseErrorMapper {
    fun mapError(error: Response.Error<TransitAgency>)
}