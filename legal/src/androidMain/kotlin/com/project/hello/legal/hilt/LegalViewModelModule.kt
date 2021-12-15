package com.project.hello.legal.hilt

import com.project.hello.legal.datasaver.LatestAcceptedLegalDataSaver
import com.project.hello.legal.datasource.AvailableLegalDataSource
import com.project.hello.legal.datasource.LatestAcceptedLegalDataSource
import com.project.hello.legal.internal.datasaver.PrefsLatestAcceptedLegalDataSaverImpl
import com.project.hello.legal.internal.datasource.PrefsLatestAcceptedLegalDataSourceImpl
import com.project.hello.legal.internal.datasource.RawResourcesLatestAvailableLegalDataSourceImpl
import com.project.hello.legal.internal.usecase.*
import com.project.hello.legal.internal.usecase.implementation.LatestAcceptedLegalVersionUseCaseImpl
import com.project.hello.legal.internal.usecase.implementation.LatestAvailableLegalSaverUseCaseImpl
import com.project.hello.legal.internal.usecase.implementation.LatestAvailableLegalUseCaseImpl
import com.project.hello.legal.repository.LegalRepository
import com.project.hello.legal.repository.implementation.LegalRepositoryImpl
import com.project.hello.legal.usecase.LatestAcceptedLegalUseCaseErrorMapper
import com.project.hello.legal.usecase.LatestAvailableLegalUseCaseErrorMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class LegalViewModelModule {

    @Binds
    abstract fun bindsAvailableLegalDataSource(
        impl: RawResourcesLatestAvailableLegalDataSourceImpl
    ): AvailableLegalDataSource

    @Binds
    abstract fun bindsLatestAcceptedLegalDataSource(
        impl: PrefsLatestAcceptedLegalDataSourceImpl
    ): LatestAcceptedLegalDataSource

    @Binds
    abstract fun bindLatestAcceptedLegalDataSaver(
        impl: PrefsLatestAcceptedLegalDataSaverImpl
    ): LatestAcceptedLegalDataSaver

    @Binds
    abstract fun bindLatestAcceptedLegalUseCaseErrorMapper(
        impl: LatestAcceptedLegalUseCaseErrorMapperImpl
    ): LatestAcceptedLegalUseCaseErrorMapper

    @Binds
    abstract fun bindLatestAvailableLegalUseCaseErrorMapper(
        impl: LatestAvailableLegalUseCaseErrorMapperImpl
    ): LatestAvailableLegalUseCaseErrorMapper

    @Binds
    abstract fun bindLatestAvailableLegalSaverUseCase(impl: LatestAvailableLegalSaverUseCaseImpl)
            : LatestAvailableLegalSaverUseCase

    @Binds
    abstract fun bindLatestAcceptedLegalVersionUseCase(impl: LatestAcceptedLegalVersionUseCaseImpl):
            LatestAcceptedLegalVersionUseCase

    @Binds
    abstract fun bindLatestAvailableLegalUseCase(impl: LatestAvailableLegalUseCaseImpl):
            LatestAvailableLegalUseCase

    companion object {

        @Provides
        fun provideLegalRepository(
            availableLegalDataSource: AvailableLegalDataSource,
            latestAcceptedLegalDataSource: LatestAcceptedLegalDataSource,
            latestAcceptedLegalDataSaver: LatestAcceptedLegalDataSaver
        ): LegalRepository = LegalRepositoryImpl(
            availableLegalDataSource,
            latestAcceptedLegalDataSource,
            latestAcceptedLegalDataSaver
        )
    }
}