package com.project.hello.legal.domain.usecase

import com.project.hello.commons.domain.data.Response
import com.project.hello.legal.domain.model.LatestAvailableLegal

interface LatestAvailableLegalUseCaseErrorMapper {
    fun mapError(error: Response.Error<LatestAvailableLegal>)
}