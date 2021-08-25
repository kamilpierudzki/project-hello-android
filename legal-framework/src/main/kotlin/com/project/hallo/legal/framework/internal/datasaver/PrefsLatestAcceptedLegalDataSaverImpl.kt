package com.project.hallo.legal.framework.internal.datasaver

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.project.hallo.commons.domain.data.ResponseApi
import com.project.hallo.commons.framework.hilt.AppSharedPreferences
import com.project.hallo.legal.framework.internal.LegalPreferencesConstants.LATEST_ACCEPTED_LEGAL_VERSION
import com.project.hello.legal.domain.datasaver.LatestAcceptedLegalDataSaver
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi
import javax.inject.Inject

internal class PrefsLatestAcceptedLegalDataSaverImpl @Inject constructor(
    @AppSharedPreferences private val sharedPreferences: SharedPreferences
) : LatestAcceptedLegalDataSaver {

    @SuppressLint("ApplySharedPref")
    override fun saveLatestAcceptedLegal(latestAvailableLegalApi: LatestAvailableLegalApi): ResponseApi<Unit> {
        sharedPreferences.edit().apply {
            putInt(LATEST_ACCEPTED_LEGAL_VERSION, latestAvailableLegalApi.version)
            commit()
        }
        return ResponseApi.Success(Unit)
    }
}