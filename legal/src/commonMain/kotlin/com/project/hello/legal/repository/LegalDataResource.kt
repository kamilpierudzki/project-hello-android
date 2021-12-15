package com.project.hello.legal.repository

import com.project.hello.commons.data.ResponseApi
import com.project.hello.legal.model.LatestAvailableLegal
import com.project.hello.legal.model.api.LatestAvailableLegalApi

interface LegalDataResource {
    fun latestAvailableLegal(): ResponseApi<LatestAvailableLegalApi>
    fun latestAcceptedLegalVersion(): ResponseApi<Int>
    fun saveLatestAcceptedLegal(latestAvailableLegal: LatestAvailableLegal): ResponseApi<Unit>
}