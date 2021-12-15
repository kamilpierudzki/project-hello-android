package com.project.hello.welcome.internal.datasource

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.project.hello.commons.data.ResponseApi
import com.project.hello.commons.hilt.AppSharedPreferences
import com.project.hello.welcome.datasource.FirstLaunchDataSaver
import com.project.hello.welcome.internal.WelcomePreferencesConstants.FIRST_LAUNCH
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