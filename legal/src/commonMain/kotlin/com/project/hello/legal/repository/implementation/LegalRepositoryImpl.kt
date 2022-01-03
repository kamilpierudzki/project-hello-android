package com.project.hello.legal.repository.implementation

import com.project.hello.commons.data.ResponseApi
import com.project.hello.legal.datasaver.LatestAcceptedLegalDataSaver
import com.project.hello.legal.datasource.AvailableLegalDataSource
import com.project.hello.legal.datasource.LatestAcceptedLegalDataSource
import com.project.hello.legal.model.LatestAvailableLegal
import com.project.hello.legal.model.api.LatestAvailableLegalApi
import com.project.hello.legal.repository.LegalDataResource
import com.project.hello.legal.repository.LegalRepository

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

private fun LatestAvailableLegal.toLatestAvailableLegalApi() =
    LatestAvailableLegalApi(version = version, message = message)