package com.project.hello.legal.framework.internal.datasource

import android.content.SharedPreferences
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.hilt.AppSharedPreferences
import com.project.hello.legal.framework.internal.LegalPreferencesConstants.LATEST_ACCEPTED_LEGAL_VERSION
import com.project.hello.legal.domain.datasource.LatestAcceptedLegalDataSource
import com.project.hello.legal.domain.model.LegalErrorCode.LatestAcceptedLegal.LATEST_ACCEPTED_LEGAL_ERROR
import javax.inject.Inject

internal class PrefsLatestAcceptedLegalDataSourceImpl @Inject constructor(
    @AppSharedPreferences private val sharedPreferences: SharedPreferences
) : LatestAcceptedLegalDataSource {

    override fun fetchLatestAcceptedLegalVersion(): ResponseApi<Int> {
        val latestAcceptedLegalVersion = sharedPreferences.getInt(LATEST_ACCEPTED_LEGAL_VERSION, -1)
        return if (latestAcceptedLegalVersion == -1) {
            ResponseApi.Error(LATEST_ACCEPTED_LEGAL_ERROR)
        } else {
            ResponseApi.Success(latestAcceptedLegalVersion)
        }
    }
}