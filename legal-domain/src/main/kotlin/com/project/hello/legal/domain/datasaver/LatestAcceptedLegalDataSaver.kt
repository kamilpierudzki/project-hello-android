package com.project.hello.legal.domain.datasaver

import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi

interface LatestAcceptedLegalDataSaver {
    fun saveLatestAcceptedLegal(latestAvailableLegalApi: LatestAvailableLegalApi): ResponseApi<Unit>
}