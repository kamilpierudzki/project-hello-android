package com.project.hello.legal.domain.usecase

import com.project.hallo.commons.domain.repository.Response

interface LatestAcceptedLegalUseCaseErrorMapper {
    fun mapError(error: Response.Error<Int>)
}