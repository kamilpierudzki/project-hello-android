package com.project.hello.legal.domain.usecase

import com.project.hallo.commons.domain.repository.Response
import com.project.hello.legal.domain.model.LatestAvailableLegal

interface LatestAvailableLegalUseCaseErrorMapper {
    fun mapError(error: Response.Error<LatestAvailableLegal>)
}