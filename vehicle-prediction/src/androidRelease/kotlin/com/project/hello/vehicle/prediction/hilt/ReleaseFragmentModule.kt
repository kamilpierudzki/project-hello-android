package com.project.hello.vehicle.prediction.hilt

import com.project.hello.vehicle.prediction.internal.ui.CityLinesInfo
import com.project.hello.vehicle.prediction.internal.ui.CityLinesInfoEmpty
import com.project.hello.vehicle.prediction.internal.ui.FpsCounterManager
import com.project.hello.vehicle.prediction.internal.ui.FpsCounterManagerEmpty
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
internal abstract class ReleaseFragmentModule {

    @Binds
    abstract fun bindFpsCounterManager(impl: FpsCounterManagerEmpty): FpsCounterManager

    @Binds
    abstract fun bindCityLinesInfo(impl: CityLinesInfoEmpty): CityLinesInfo
}