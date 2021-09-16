package com.project.hello.welcome.framework.hilt

import com.project.hello.welcome.domain.datasource.FirstLaunchDataSaver
import com.project.hello.welcome.domain.datasource.FirstLaunchDataSource
import com.project.hello.welcome.domain.repository.WelcomeRepository
import com.project.hello.welcome.domain.repository.implementation.WelcomeRepositoryImpl
import com.project.hello.welcome.framework.internal.datasource.PrefsFirstLaunchDataSaverImpl
import com.project.hello.welcome.framework.internal.datasource.PrefsFirstLaunchDataSourceImpl
import com.project.hello.welcome.framework.internal.usecase.FirstLaunchSaverUseCase
import com.project.hello.welcome.framework.internal.usecase.FirstLaunchUseCase
import com.project.hello.welcome.framework.internal.usecase.implementation.FirstLaunchSaverUseCaseImpl
import com.project.hello.welcome.framework.internal.usecase.implementation.FirstLaunchUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class WelcomeViewModelModule {

    @Binds
    abstract fun bindFirstLaunchDataSource(impl: PrefsFirstLaunchDataSourceImpl): FirstLaunchDataSource

    @Binds
    abstract fun bindFirstLaunchDataSaver(impl: PrefsFirstLaunchDataSaverImpl): FirstLaunchDataSaver

    @Binds
    abstract fun bindFirstLaunchUseCase(impl: FirstLaunchUseCaseImpl): FirstLaunchUseCase

    @Binds
    abstract fun bindFirstLaunchSaverUseCase(impl: FirstLaunchSaverUseCaseImpl):
            FirstLaunchSaverUseCase

    companion object {

        @Provides
        fun provideWelcomeRepository(
            firstLaunchDataSource: FirstLaunchDataSource,
            firstLaunchDataSaver: FirstLaunchDataSaver,
        ): WelcomeRepository = WelcomeRepositoryImpl(firstLaunchDataSource, firstLaunchDataSaver)
    }
}