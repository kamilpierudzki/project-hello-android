package com.project.hello.legal.usecase

import com.project.hello.commons.data.Response
import com.project.hello.legal.model.LatestAvailableLegal

interface LatestAvailableLegalUseCaseErrorMapper {
    fun mapError(error: Response.Error<LatestAvailableLegal>)
}