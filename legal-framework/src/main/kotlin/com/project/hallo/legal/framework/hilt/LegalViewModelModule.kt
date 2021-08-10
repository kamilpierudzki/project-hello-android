package com.project.hallo.legal.framework.hilt

import com.project.hallo.legal.framework.internal.datasource.PrefsLatestAcceptedLegalDataSourceImpl
import com.project.hallo.legal.framework.internal.datasource.RawResourcesLatestAvailableLegalDataSourceImpl
import com.project.hello.legal.domain.datasource.AvailableLegalDataSource
import com.project.hello.legal.domain.datasource.LatestAcceptedLegalDataSource
import com.project.hello.legal.domain.repository.LegalRepository
import com.project.hello.legal.domain.repository.implementation.LegalRepositoryImpl
import dagger.Binds
import dagger.Module
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
    abstract fun bindLegalRepository(impl: LegalRepositoryImpl): LegalRepository
}