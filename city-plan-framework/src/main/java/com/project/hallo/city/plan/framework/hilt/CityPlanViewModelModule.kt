package com.project.hallo.city.plan.framework.hilt

import android.content.res.Resources
import com.project.hallo.city.plan.domain.CityPlanRepository
import com.project.hallo.city.plan.domain.datasource.CityDataSource
import com.project.hallo.city.plan.domain.datasource.CityPlanDataSource
import com.project.hallo.city.plan.domain.datasource.SupportedCityDataSource
import com.project.hallo.city.plan.domain.repository.SupportedCitiesRepository
import com.project.hallo.city.plan.domain.usecase.CityPlanUseCase
import com.project.hallo.city.plan.domain.usecase.CityUseCase
import com.project.hallo.city.plan.domain.usecase.SupportedCitiesUseCase
import com.project.hallo.city.plan.domain.usecase.implementation.CityPlanUseCaseImpl
import com.project.hallo.city.plan.domain.usecase.implementation.CityUseCaseImpl
import com.project.hallo.city.plan.domain.usecase.implementation.SupportedCitiesUseCaseImpl
import com.project.hallo.city.plan.framework.internal.datasource.RawResourceCityPlanDataSourceImpl
import com.project.hallo.city.plan.framework.internal.datasource.RawResourcesCityDataSourceImpl
import com.project.hallo.city.plan.framework.internal.datasource.RawResourcesSupportedCityDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class CityPlanViewModelModule {

    @Provides
    fun provideRawResourceCityPlanDataSource(resources: Resources): CityPlanDataSource =
        RawResourceCityPlanDataSourceImpl(resources)

    @Provides
    fun provideCityPlanRepository(cityPlanDataSource: CityPlanDataSource) =
        CityPlanRepository(cityPlanDataSource)

    @Provides
    fun provideCityPlanUseCase(cityPlanRepository: CityPlanRepository): CityPlanUseCase =
        CityPlanUseCaseImpl(cityPlanRepository)

    @Provides
    fun provideRawResourcesSupportedCityDataSourceImpl(resources: Resources): SupportedCityDataSource =
        RawResourcesSupportedCityDataSourceImpl(resources)

    @Provides
    fun provideRawResourcesCityDataSourceImpl(resources: Resources): CityDataSource =
        RawResourcesCityDataSourceImpl(resources)

    @Provides
    fun provideSupportedCitiesRepository(supportedCityDataSource: SupportedCityDataSource):
            SupportedCitiesRepository = SupportedCitiesRepository(supportedCityDataSource)

    @Provides
    fun provideSupportedCitiesUseCase(supportedCitiesRepository: SupportedCitiesRepository):
            SupportedCitiesUseCase = SupportedCitiesUseCaseImpl(supportedCitiesRepository)

    @Provides
    fun provideCityUseCase(): CityUseCase = CityUseCaseImpl()
}