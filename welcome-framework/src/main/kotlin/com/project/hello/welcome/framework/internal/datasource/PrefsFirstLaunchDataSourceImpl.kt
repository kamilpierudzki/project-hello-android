package com.project.hello.welcome.framework.internal.datasource

import android.content.SharedPreferences
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.hilt.AppSharedPreferences
import com.project.hello.welcome.domain.datasource.FirstLaunchDataSource
import com.project.hello.welcome.domain.model.WelcomeErrorCode.FirstLaunch.FIRST_LAUNCH_DATA_NOT_FOUND
import com.project.hello.welcome.framework.internal.WelcomePreferencesConstants.FIRST_LAUNCH
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