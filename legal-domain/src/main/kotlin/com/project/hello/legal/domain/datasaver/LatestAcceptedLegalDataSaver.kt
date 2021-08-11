package com.project.hello.legal.domain.datasaver

import com.project.hallo.commons.domain.repository.Response
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi

interface LatestAcceptedLegalDataSaver {
    fun saveLatestAcceptedLegal(latestAvailableLegalApi: LatestAvailableLegalApi): Response<Unit>
}