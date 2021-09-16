package com.project.hello.welcome.framework.api

import androidx.lifecycle.LiveData
import com.project.hello.commons.framework.viewmodel.ExternalViewModel

interface WelcomeViewModel : ExternalViewModel {
    val isFirstLaunch: LiveData<Boolean>

    fun markFirstLaunchAccomplished()
}