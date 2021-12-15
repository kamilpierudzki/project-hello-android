package com.project.hello.welcome.datasource

import com.project.hello.commons.data.ResponseApi

interface FirstLaunchDataSource {
    fun isFirstLaunch(): ResponseApi<Boolean>
}