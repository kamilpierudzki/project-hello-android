package com.project.hallo.city.plan.framework.hilt

import android.content.res.Resources
import com.project.hallo.city.plan.domain.datasource.CityDataSource
import com.project.hallo.city.plan.domain.repository.CityPlanRepository
import com.project.hallo.city.plan.domain.repository.implementation.CityPlanRepositoryImpl
import com.project.hallo.city.plan.domain.usecase.CityPlanUseCase
import com.project.hallo.city.plan.domain.usecase.CitySelectionUseCase
import com.project.hallo.city.plan.domain.usecase.SelectedCityUseCase
import com.project.hallo.city.plan.domain.usecase.SupportedCitiesUseCase
import com.project.hallo.city.plan.domain.usecase.implementation.CityPlanUseCaseImpl
import com.project.hallo.city.plan.domain.usecase.implementation.CitySelectionUseCaseImpl
import com.project.hallo.city.plan.domain.usecase.implementation.SelectedCityUseCaseImpl
import com.project.hallo.city.plan.domain.usecase.implementation.SupportedCitiesUseCaseImpl
import com.project.hallo.city.plan.framework.internal.datasource.RawResourcesCityDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal class CityPlanViewModelModule {

    @Provides
    fun provideCityPlanUseCase(cityPlanRepository: CityPlanRepository): CityPlanUseCase =
        CityPlanUseCaseImpl(cityPlanRepository)

    @Provides
    fun provideRawResourcesCityDataSourceImpl(resources: Resources): CityDataSource =
        RawResourcesCityDataSourceImpl(resources)

    @Provides
    fun provideCityPlanRepositoryNew(cityDataSource: CityDataSource): CityPlanRepository =
        CityPlanRepositoryImpl(cityDataSource)

    @Provides
    fun provideSupportedCitiesUseCase(cityPlanRepository: CityPlanRepository):
            SupportedCitiesUseCase = SupportedCitiesUseCaseImpl(cityPlanRepository)

    @Provides
    fun provideCityUseCase(cityPlanRepository: CityPlanRepository): CitySelectionUseCase =
        CitySelectionUseCaseImpl(cityPlanRepository)

    @Provides
    fun provideSelectedCityUseCase(cityPlanRepository: CityPlanRepository): SelectedCityUseCase =
        SelectedCityUseCaseImpl(cityPlanRepository)
}