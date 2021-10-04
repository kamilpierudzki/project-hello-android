package com.project.hello.city.plan.framework.hilt

import android.content.res.Resources
import com.project.hello.city.plan.domain.datasource.SelectedTransitAgencyDataSource
import com.project.hello.city.plan.domain.datasource.TransitAgencyDataSource
import com.project.hello.city.plan.domain.repository.TransitAgencyPlanRepository
import com.project.hello.city.plan.domain.repository.implementation.TransitAgencyPlanRepositoryImpl
import com.project.hello.city.plan.domain.usecase.SelectedTransitAgencyUseCaseErrorMapper
import com.project.hello.city.plan.domain.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import com.project.hello.city.plan.framework.internal.datasource.RawResourcesTransitAgencyDataSourceImpl
import com.project.hello.city.plan.framework.internal.datasource.db.DataBaseSelectedTransitAgency
import com.project.hello.city.plan.framework.internal.db.TransitAgencyDatabase
import com.project.hello.city.plan.framework.internal.usecase.*
import com.project.hello.city.plan.framework.internal.usecase.implementation.SelectedTransitAgencyUseCaseImpl
import com.project.hello.city.plan.framework.internal.usecase.implementation.SupportedTransitAgenciesUseCaseImpl
import com.project.hello.city.plan.framework.internal.usecase.implementation.TransitAgencySelectionUseCaseImpl
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

    companion object {
        @Provides
        fun provideCityDataSource(resources: Resources): TransitAgencyDataSource =
            RawResourcesTransitAgencyDataSourceImpl(resources)

        @Provides
        fun provideSelectedCityDataSource(
            database: TransitAgencyDatabase
        ): SelectedTransitAgencyDataSource = DataBaseSelectedTransitAgency(database)

        @Provides
        fun provideTransitAgencyPlanRepositoryNew(
            transitAgencyDataSource: TransitAgencyDataSource,
            selectedTransitAgencyDataSource: SelectedTransitAgencyDataSource
        ): TransitAgencyPlanRepository =
            TransitAgencyPlanRepositoryImpl(
                transitAgencyDataSource,
                selectedTransitAgencyDataSource
            )
    }
}