package com.project.hello.welcome.hilt

import com.project.hello.welcome.datasource.FirstLaunchDataSaver
import com.project.hello.welcome.datasource.FirstLaunchDataSource
import com.project.hello.welcome.repository.WelcomeRepository
import com.project.hello.welcome.repository.implementation.WelcomeRepositoryImpl
import com.project.hello.welcome.internal.datasource.PrefsFirstLaunchDataSaverImpl
import com.project.hello.welcome.internal.datasource.PrefsFirstLaunchDataSourceImpl
import com.project.hello.welcome.internal.usecase.FirstLaunchSaverUseCase
import com.project.hello.welcome.internal.usecase.FirstLaunchUseCase
import com.project.hello.welcome.internal.usecase.implementation.FirstLaunchSaverUseCaseImpl
import com.project.hello.welcome.internal.usecase.implementation.FirstLaunchUseCaseImpl
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