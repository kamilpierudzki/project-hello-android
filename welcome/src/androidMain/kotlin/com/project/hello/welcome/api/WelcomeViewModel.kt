package com.project.hello.welcome.api

import androidx.lifecycle.LiveData
import com.project.hello.commons.livedata.Event
import com.project.hello.commons.viewmodel.ExternalViewModel

interface WelcomeViewModel : ExternalViewModel {
    val isFirstLaunch: LiveData<Event<Boolean>>

    fun markFirstLaunchAccomplished()
}