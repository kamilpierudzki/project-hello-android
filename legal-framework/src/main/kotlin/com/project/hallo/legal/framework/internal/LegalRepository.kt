package com.project.hallo.legal.framework.internal

import com.project.hallo.commons.domain.repository.Response
import javax.inject.Inject

internal class LegalRepository @Inject constructor(
    private val resourcesSource: LegalDataSource
) {

    fun getLegalDataResource() = object : LegalDataResource {
        override fun latestAvailableLegal(): Response<LegalApi> {
            return resourcesSource.fetchAvailableLegal()
        }

        override fun latestAcceptedLegal(): Response<LegalApi> {
            TODO("Not yet implemented")
        }
    }
}