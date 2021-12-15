package com.project.hello.vehicle.prediction.hilt

import com.project.hello.vehicle.prediction.internal.ui.CityLinesInfo
import com.project.hello.vehicle.prediction.internal.ui.DebugCityLinesInfoImpl
import com.project.hello.vehicle.prediction.internal.ui.DebugFpsCounterManagerImpl
import com.project.hello.vehicle.prediction.internal.ui.FpsCounterManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
internal abstract class DebugFragmentModule {

    @Binds
    abstract fun bindFpsCounterManager(debugImpl: DebugFpsCounterManagerImpl): FpsCounterManager

    @Binds
    abstract fun bindDebugCityLinesInfo(impl: DebugCityLinesInfoImpl): CityLinesInfo
}