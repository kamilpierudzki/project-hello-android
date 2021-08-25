package com.project.hello.legal.domain.repository.implementation

import com.project.hallo.commons.domain.data.ResponseApi
import com.project.hello.legal.domain.datasaver.LatestAcceptedLegalDataSaver
import com.project.hello.legal.domain.datasource.AvailableLegalDataSource
import com.project.hello.legal.domain.datasource.LatestAcceptedLegalDataSource
import com.project.hello.legal.domain.model.LatestAvailableLegal
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi
import com.project.hello.legal.domain.model.toLatestAvailableLegalApi
import com.project.hello.legal.domain.repository.LegalDataResource
import com.project.hello.legal.domain.repository.LegalRepository

class LegalRepositoryImpl(
    private val availableLegalDataSource: AvailableLegalDataSource,
    private val latestAcceptedLegalDataSource: LatestAcceptedLegalDataSource,
    private val latestAcceptedLegalDataSaver: LatestAcceptedLegalDataSaver
) : LegalRepository {

    override fun getLegalDataResource() = object : LegalDataResource {
        override fun latestAcceptedLegalVersion(): ResponseApi<Int> {
            return latestAcceptedLegalDataSource.fetchLatestAcceptedLegalVersion()
        }

        override fun latestAvailableLegal(): ResponseApi<LatestAvailableLegalApi> {
            return availableLegalDataSource.fetchAvailableLegal()
        }

        override fun saveLatestAcceptedLegal(latestAvailableLegal: LatestAvailableLegal): ResponseApi<Unit> {
            val latestAvailableLegalApi = latestAvailableLegal.toLatestAvailableLegalApi()
            return latestAcceptedLegalDataSaver.saveLatestAcceptedLegal(latestAvailableLegalApi)
        }
    }
}