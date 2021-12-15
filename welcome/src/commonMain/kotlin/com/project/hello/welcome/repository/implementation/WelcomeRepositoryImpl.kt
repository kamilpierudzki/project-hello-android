package com.project.hello.welcome.repository.implementation

import com.project.hello.commons.data.ResponseApi
import com.project.hello.welcome.datasource.FirstLaunchDataSaver
import com.project.hello.welcome.datasource.FirstLaunchDataSource
import com.project.hello.welcome.repository.WelcomeRepository
import com.project.hello.welcome.repository.WelcomeResource

class WelcomeRepositoryImpl(
    private val firstLaunchDataSource: FirstLaunchDataSource,
    private val firstLaunchDataSaver: FirstLaunchDataSaver
) : WelcomeRepository {

    override fun getWelcomeResource() = object : WelcomeResource {
        override fun isFirstLaunch(): ResponseApi<Boolean> {
            return firstLaunchDataSource.isFirstLaunch()
        }

        override fun saveFirstLaunch(): ResponseApi<Unit> {
            return firstLaunchDataSaver.saveFirstLaunch()
        }
    }
}