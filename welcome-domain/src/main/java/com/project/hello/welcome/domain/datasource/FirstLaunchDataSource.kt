package com.project.hello.welcome.domain.datasource

import com.project.hello.commons.domain.data.ResponseApi

interface FirstLaunchDataSource {
    fun isFirstLaunch(): ResponseApi<Boolean>
}