package com.project.hello.city.plan.framework.hilt

import android.content.res.Resources
import com.project.hello.city.plan.domain.datasource.CityDataSource
import com.project.hello.city.plan.domain.datasource.SelectedCityDataSource
import com.project.hello.city.plan.domain.repository.CityPlanRepository
import com.project.hello.city.plan.domain.repository.implementation.CityPlanRepositoryImpl
import com.project.hello.city.plan.domain.usecase.*
import com.project.hello.city.plan.domain.usecase.implementation.*
import com.project.hello.city.plan.framework.internal.datasource.RawResourcesCityDataSourceImpl
import com.project.hello.city.plan.framework.internal.datasource.db.DataBaseSelectedCity
import com.project.hello.city.plan.framework.internal.db.CityDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal class CityPlanViewModelModule {

    @Provides
    fun provideCityDataSource(resources: Resources): CityDataSource =
        RawResourcesCityDataSourceImpl(resources)

    @Provides
    fun provideSelectedCityDataSource(database: CityDatabase): SelectedCityDataSource =
        DataBaseSelectedCity(database)

    @Provides
    fun provideCityPlanRepositoryNew(
        cityDataSource: CityDataSource,
        selectedCityDataSource: SelectedCityDataSource
    ): CityPlanRepository =
        CityPlanRepositoryImpl(cityDataSource, selectedCityDataSource)

    @Provides
    fun provideSupportedCitiesUseCaseErrorMapper(resources: Resources): SupportedCitiesUseCaseErrorMapper =
        SupportedCitiesUseCaseErrorMapperImpl(resources)

    @Provides
    fun provideSupportedCitiesUseCase(
        cityPlanRepository: CityPlanRepository,
        supportedCitiesUseCaseErrorMapper: SupportedCitiesUseCaseErrorMapper
    ): SupportedCitiesUseCase =
        SupportedCitiesUseCaseImpl(cityPlanRepository, supportedCitiesUseCaseErrorMapper)

    @Provides
    fun provideCityUseCase(cityPlanRepository: CityPlanRepository): CitySelectionUseCase =
        CitySelectionUseCaseImpl(cityPlanRepository)

    @Provides
    fun provideSelectedCityUseCaseErrorMapper(resources: Resources): SelectedCityUseCaseErrorMapper =
        SelectedCityUseCaseErrorMapperImpl(resources)

    @Provides
    fun provideSelectedCityUseCase(
        cityPlanRepository: CityPlanRepository,
        selectedCityUseCaseErrorMapper: SelectedCityUseCaseErrorMapper
    ): SelectedCityUseCase =
        SelectedCityUseCaseImpl(cityPlanRepository, selectedCityUseCaseErrorMapper)
}