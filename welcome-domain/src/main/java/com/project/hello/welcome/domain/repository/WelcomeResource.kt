package com.project.hello.welcome.domain.repository

import com.project.hello.commons.domain.data.ResponseApi

interface WelcomeResource {
    fun isFirstLaunch(): ResponseApi<Boolean>
    fun saveFirstLaunch(): ResponseApi<Unit>
}