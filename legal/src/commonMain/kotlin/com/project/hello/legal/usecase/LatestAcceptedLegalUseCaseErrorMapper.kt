package com.project.hello.legal.usecase

import com.project.hello.commons.data.Response

interface LatestAcceptedLegalUseCaseErrorMapper {
    fun mapError(error: Response.Error<Int>)
}