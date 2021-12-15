package com.project.hello.welcome.datasource

import com.project.hello.commons.data.ResponseApi

interface FirstLaunchDataSaver {
    fun saveFirstLaunch(): ResponseApi<Unit>
}