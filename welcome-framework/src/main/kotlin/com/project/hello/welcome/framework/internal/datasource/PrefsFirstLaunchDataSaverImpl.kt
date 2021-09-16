package com.project.hello.welcome.framework.internal.datasource

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.hilt.AppSharedPreferences
import com.project.hello.welcome.domain.datasource.FirstLaunchDataSaver
import com.project.hello.welcome.framework.internal.WelcomePreferencesConstants.FIRST_LAUNCH
import javax.inject.Inject

internal class PrefsFirstLaunchDataSaverImpl @Inject constructor(
    @AppSharedPreferences private val sharedPreferences: SharedPreferences
) : FirstLaunchDataSaver {

    @SuppressLint("ApplySharedPref")
    override fun saveFirstLaunch(): ResponseApi<Unit> {
        sharedPreferences.edit().apply {
            putBoolean(FIRST_LAUNCH, false)
            commit()
        }
        return ResponseApi.Success(Unit)
    }
}