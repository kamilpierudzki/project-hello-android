package com.project.hello.transit.agency.hilt

import com.project.hello.transit.agency.internal.cryptography.Decoding
import com.project.hello.transit.agency.internal.cryptography.implementation.DecodingImpl
import com.project.hello.transit.agency.internal.repository.TransitAgencyPlanRepositoryImpl
import com.project.hello.transit.agency.internal.repository.datasource.LocalSelectedTransitAgencyDataSource
import com.project.hello.transit.agency.internal.repository.datasource.LocalSupportedTransitAgenciesDataSource
import com.project.hello.transit.agency.internal.repository.db.TransitAgencyDatabase
import com.project.hello.transit.agency.internal.usecase.*
import com.project.hello.transit.agency.internal.usecase.implementation.SelectedTransitAgencyUseCaseImpl
import com.project.hello.transit.agency.internal.usecase.implementation.SupportedTransitAgenciesUseCaseImpl
import com.project.hello.transit.agency.internal.usecase.implementation.TransitAgencySelectionUseCaseImpl
import com.project.hello.transit.agency.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.repository.datasource.SelectedTransitAgencyDataSource
import com.project.hello.transit.agency.repository.datasource.SupportedTransitAgenciesDataSource
import com.project.hello.transit.agency.usecase.SelectedTransitAgencyUseCaseErrorMapper
import com.project.hello.transit.agency.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class TransitAgencyViewModelModule {

    @Binds
    abstract fun bindSelectedCityUseCaseErrorMapper(
        impl: SelectedTransitAgencyUseCaseErrorMapperImpl
    ): SelectedTransitAgencyUseCaseErrorMapper

    @Binds
    abstract fun bindSupportedCitiesUseCaseErrorMapper(
        impl: SupportedTransitAgenciesUseCaseErrorMapperImpl
    ): SupportedTransitAgenciesUseCaseErrorMapper

    @Binds
    abstract fun bindSupportedTransitAgencyUseCase(impl: SupportedTransitAgenciesUseCaseImpl):
            SupportedTransitAgenciesUseCase

    @Binds
    abstract fun bindSelectedTransitAgencyUseCase(impl: SelectedTransitAgencyUseCaseImpl):
            SelectedTransitAgencyUseCase

    @Binds
    abstract fun bindTransitAgencySelectionUseCase(impl: TransitAgencySelectionUseCaseImpl):
            TransitAgencySelectionUseCase

    @Binds
    abstract fun bindSupportedTransitAgenciesDataSource(
        impl: LocalSupportedTransitAgenciesDataSource
    ): SupportedTransitAgenciesDataSource

    @Binds
    abstract fun bindTransitAgencyPlanRepository(impl: TransitAgencyPlanRepositoryImpl):
            TransitAgencyPlanRepository

    @Binds
    abstract fun bindDecoding(impl: DecodingImpl): Decoding

    companion object {

        @Provides
        fun provideSelectedCityDataSource(
            database: TransitAgencyDatabase
        ): SelectedTransitAgencyDataSource = LocalSelectedTransitAgencyDataSource(database)
    }
}