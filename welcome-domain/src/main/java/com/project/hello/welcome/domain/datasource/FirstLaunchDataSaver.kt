package com.project.hello.welcome.domain.datasource

import com.project.hello.commons.domain.data.ResponseApi

interface FirstLaunchDataSaver {
    fun saveFirstLaunch(): ResponseApi<Unit>
}