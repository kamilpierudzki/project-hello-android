package com.project.hello.legal.domain.repository

import com.project.hallo.commons.domain.data.ResponseApi
import com.project.hello.legal.domain.model.LatestAvailableLegal
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi

interface LegalDataResource {
    fun latestAvailableLegal(): ResponseApi<LatestAvailableLegalApi>
    fun latestAcceptedLegalVersion(): ResponseApi<Int>
    fun saveLatestAcceptedLegal(latestAvailableLegal: LatestAvailableLegal): ResponseApi<Unit>
}