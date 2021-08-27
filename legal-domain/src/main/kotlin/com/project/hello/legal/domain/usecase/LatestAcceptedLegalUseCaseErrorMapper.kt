package com.project.hello.legal.domain.usecase

import com.project.hello.commons.domain.data.Response

interface LatestAcceptedLegalUseCaseErrorMapper {
    fun mapError(error: Response.Error<Int>)
}