package com.project.hello.welcome.domain.repository.implementation

import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.welcome.domain.datasource.FirstLaunchDataSaver
import com.project.hello.welcome.domain.datasource.FirstLaunchDataSource
import com.project.hello.welcome.domain.repository.WelcomeRepository
import com.project.hello.welcome.domain.repository.WelcomeResource

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