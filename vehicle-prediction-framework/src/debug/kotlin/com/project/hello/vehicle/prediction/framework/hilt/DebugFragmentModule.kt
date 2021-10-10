package com.project.hello.vehicle.prediction.framework.hilt

import com.project.hello.vehicle.prediction.framework.internal.ui.CityLinesInfo
import com.project.hello.vehicle.prediction.framework.internal.ui.DebugCityLinesInfoImpl
import com.project.hello.vehicle.prediction.framework.internal.ui.FpsCounterManager
import com.project.hello.vehicle.prediction.framework.internal.ui.DebugFpsCounterManagerImpl
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