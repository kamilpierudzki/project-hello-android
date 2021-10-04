package com.project.hello.city.plan.domain.usecase

import com.project.hello.city.plan.domain.model.TransitAgency
import com.project.hello.commons.domain.data.Response

interface SelectedTransitAgencyUseCaseErrorMapper {
    fun mapError(error: Response.Error<TransitAgency>)
}