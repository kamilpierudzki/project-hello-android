package com.project.hallo.legal.framework.internal

import com.project.hallo.commons.domain.repository.Response
import javax.inject.Inject

internal class LegalRepository @Inject constructor(
    private val availableLegalDataSource: AvailableLegalDataSource,
    private val latestAcceptedLegalDataSource: LatestAcceptedLegalDataSource
) {

    fun getLegalDataResource() = object : LegalDataResource {
        override fun latestAvailableLegal(): Response<LegalApi> {
            return availableLegalDataSource.fetchAvailableLegal()
        }

        override fun latestAcceptedLegal(): Response<LegalApi> {
            return latestAcceptedLegalDataSource.fetchLatestAcceptedLegal()
        }
    }
}