package com.project.hallo.city.plan.framework.hilt

import com.project.hallo.city.plan.framework.internal.ui.CityPickerAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
internal class CityPlanFragmentModule {

    @Provides
    fun provideCityPickerAdapter() = CityPickerAdapter()
}