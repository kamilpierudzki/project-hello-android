package com.project.hallo.city.plan.framework.hilt

import android.content.res.Resources
import com.project.hallo.city.plan.domain.CityPlanDataSource
import com.project.hallo.city.plan.domain.CityPlanRepository
import com.project.hallo.city.plan.domain.usecase.CityPlanUseCase
import com.project.hallo.city.plan.domain.usecase.implementation.CityPlanUseCaseImpl
import com.project.hallo.city.plan.framework.internal.datasource.RawResourceCityPlanDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal class CityPlanViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideRawResourceCityPlanDataSource(resources: Resources): CityPlanDataSource =
        RawResourceCityPlanDataSourceImpl(resources)

    @Provides
    @ViewModelScoped
    fun provideCityPlanRepository(cityPlanDataSource: CityPlanDataSource) =
        CityPlanRepository(cityPlanDataSource)

    @Provides
    @ViewModelScoped
    fun provideCityPlanUseCase(cityPlanRepository: CityPlanRepository): CityPlanUseCase =
        CityPlanUseCaseImpl(cityPlanRepository)
}