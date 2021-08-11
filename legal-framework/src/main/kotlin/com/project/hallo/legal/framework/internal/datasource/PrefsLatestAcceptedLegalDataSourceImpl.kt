package com.project.hallo.legal.framework.internal.datasource

import android.content.SharedPreferences
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.framework.hilt.AppSharedPreferences
import com.project.hallo.legal.framework.internal.LegalPreferencesConstants.LATEST_ACCEPTED_LEGAL_VERSION
import com.project.hello.legal.domain.datasource.LatestAcceptedLegalDataSource
import com.project.hello.legal.domain.model.LegalErrorCode.LatestAcceptedLegal.LATEST_ACCEPTED_LEGAL_ERROR
import javax.inject.Inject

internal class PrefsLatestAcceptedLegalDataSourceImpl @Inject constructor(
    @AppSharedPreferences private val sharedPreferences: SharedPreferences
) : LatestAcceptedLegalDataSource {

    override fun fetchLatestAcceptedLegalVersion(): Response<Int> {
        val latestAcceptedLegalVersion = sharedPreferences.getInt(LATEST_ACCEPTED_LEGAL_VERSION, -1)
        return if (latestAcceptedLegalVersion == -1) {
            Response.Error(LATEST_ACCEPTED_LEGAL_ERROR)
        } else {
            Response.Success(latestAcceptedLegalVersion)
        }
    }
}