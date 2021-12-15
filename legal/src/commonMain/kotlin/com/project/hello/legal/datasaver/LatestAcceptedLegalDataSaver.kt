package com.project.hello.legal.datasaver

import com.project.hello.commons.data.ResponseApi
import com.project.hello.legal.model.api.LatestAvailableLegalApi

interface LatestAcceptedLegalDataSaver {
    fun saveLatestAcceptedLegal(latestAvailableLegalApi: LatestAvailableLegalApi): ResponseApi<Unit>
}