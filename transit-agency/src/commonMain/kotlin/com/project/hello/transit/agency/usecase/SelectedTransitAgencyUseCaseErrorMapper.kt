package com.project.hello.transit.agency.usecase

import com.project.hello.commons.data.Response
import com.project.hello.transit.agency.model.TransitAgency

interface SelectedTransitAgencyUseCaseErrorMapper {
    fun mapError(error: Response.Error<TransitAgency>)
}