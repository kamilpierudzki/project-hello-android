package com.project.hello.legal.domain.repository

import com.project.hallo.commons.domain.repository.Response
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi

interface LegalDataResource {
    fun latestAvailableLegal(): Response<LatestAvailableLegalApi>
    fun latestAcceptedLegalVersion(): Response<Int>
}