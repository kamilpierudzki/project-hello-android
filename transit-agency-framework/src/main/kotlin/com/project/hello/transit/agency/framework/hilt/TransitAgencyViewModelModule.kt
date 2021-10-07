package com.project.hello.transit.agency.framework.hilt

import android.content.res.Resources
import com.project.hello.transit.agency.domain.datasource.SelectedTransitAgencyDataSource
import com.project.hello.transit.agency.domain.datasource.SupportedTransitAgenciesDataSource
import com.project.hello.transit.agency.domain.datasource.SupportedTransitAgencyStopsDataSource
import com.project.hello.transit.agency.domain.usecase.SelectedTransitAgencyUseCaseErrorMapper
import com.project.hello.transit.agency.domain.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import com.project.hello.transit.agency.framework.internal.datasource.db.DataBaseSelectedTransitAgency
import com.project.hello.transit.agency.framework.internal.datasource.resources.TransitAgencyDataSource
import com.project.hello.transit.agency.framework.internal.datasource.resources.implementation.RawResourcesSupportedTransitAgenciesDataSourceImpl
import com.project.hello.transit.agency.framework.internal.datasource.resources.implementation.RawResourcesSupportedTransitAgencyStopsDataSourceImpl
import com.project.hello.transit.agency.framework.internal.datasource.resources.implementation.RawResourcesTransitAgencyDataSourceImpl
import com.project.hello.transit.agency.framework.internal.db.TransitAgencyDatabase
import com.project.hello.transit.agency.framework.internal.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.framework.internal.repository.implementation.TransitAgencyPlanRepositoryImpl
import com.project.hello.transit.agency.framework.internal.usecase.*
import com.project.hello.transit.agency.framework.internal.usecase.implementation.SelectedTransitAgencyUseCaseImpl
import com.project.hello.transit.agency.framework.internal.usecase.implementation.SupportedTransitAgenciesUseCaseImpl
import com.project.hello.transit.agency.framework.internal.usecase.implementation.TransitAgencySelectionUseCaseImpl
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
        impl: RawResourcesSupportedTransitAgenciesDataSourceImpl
    ): SupportedTransitAgenciesDataSource

    @Binds
    abstract fun bindTransitAgencyPlanRepository(impl: TransitAgencyPlanRepositoryImpl):
            TransitAgencyPlanRepository

    @Binds
    abstract fun bindSupportedTransitAgencyStopsDataSource(
        impl: RawResourcesSupportedTransitAgencyStopsDataSourceImpl
    ): SupportedTransitAgencyStopsDataSource

    companion object {
        @Provides
        fun provideCityDataSource(resources: Resources): TransitAgencyDataSource =
            RawResourcesTransitAgencyDataSourceImpl(resources)

        @Provides
        fun provideSelectedCityDataSource(
            database: TransitAgencyDatabase
        ): SelectedTransitAgencyDataSource = DataBaseSelectedTransitAgency(database)
    }
}