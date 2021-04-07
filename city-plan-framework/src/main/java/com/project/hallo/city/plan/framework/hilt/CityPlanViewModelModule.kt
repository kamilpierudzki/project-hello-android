package com.project.hallo.city.plan.framework.hilt

import com.project.hallo.city.plan.data.CityPlanDataSource
import com.project.hallo.city.plan.data.CityPlanRepository
import com.project.hallo.city.plan.data.CityPlanUseCase
import com.project.hallo.city.plan.framework.HardcodedCityPlanDataSourceImpl
import com.project.hallo.city.plan.interactor.CityPlanUseCaseImpl
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
    fun provideCityPlanDataSource(): CityPlanDataSource = HardcodedCityPlanDataSourceImpl()

    @Provides
    @ViewModelScoped
    fun provideCityPlanRepository(cityPlanDataSource: CityPlanDataSource) =
        CityPlanRepository(cityPlanDataSource)

    @Provides
    @ViewModelScoped
    fun provideCityPlanUseCase(cityPlanRepository: CityPlanRepository): CityPlanUseCase =
        CityPlanUseCaseImpl(cityPlanRepository)
}