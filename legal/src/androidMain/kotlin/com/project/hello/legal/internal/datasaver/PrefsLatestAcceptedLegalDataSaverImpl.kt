package com.project.hello.legal.internal.datasaver

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.project.hello.commons.data.ResponseApi
import com.project.hello.commons.hilt.AppSharedPreferences
import com.project.hello.legal.datasaver.LatestAcceptedLegalDataSaver
import com.project.hello.legal.internal.LegalPreferencesConstants.LATEST_ACCEPTED_LEGAL_VERSION
import com.project.hello.legal.model.api.LatestAvailableLegalApi
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