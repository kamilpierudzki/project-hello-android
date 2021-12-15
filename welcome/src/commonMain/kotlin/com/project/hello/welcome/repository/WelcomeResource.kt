package com.project.hello.welcome.repository

import com.project.hello.commons.data.ResponseApi

interface WelcomeResource {
    fun isFirstLaunch(): ResponseApi<Boolean>
    fun saveFirstLaunch(): ResponseApi<Unit>
}