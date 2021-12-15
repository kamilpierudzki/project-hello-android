package com.project.hello.welcome.internal.datasource

import android.content.SharedPreferences
import com.project.hello.commons.data.ResponseApi
import com.project.hello.commons.hilt.AppSharedPreferences
import com.project.hello.welcome.datasource.FirstLaunchDataSource
import com.project.hello.welcome.internal.WelcomePreferencesConstants.FIRST_LAUNCH
import com.project.hello.welcome.model.WelcomeErrorCode.FirstLaunch.FIRST_LAUNCH_DATA_NOT_FOUND
import javax.inject.Inject

internal class PrefsFirstLaunchDataSourceImpl @Inject constructor(
    @AppSharedPreferences private val sharedPreferences: SharedPreferences
) : FirstLaunchDataSource {

    override fun isFirstLaunch(): ResponseApi<Boolean> {
        return if (sharedPreferences.contains(FIRST_LAUNCH)) {
            val isFirstLaunch = sharedPreferences.getBoolean(FIRST_LAUNCH, true)
            ResponseApi.Success(isFirstLaunch)
        } else {
            ResponseApi.Error(FIRST_LAUNCH_DATA_NOT_FOUND)
        }
    }
}