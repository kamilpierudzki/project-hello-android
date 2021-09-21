package com.project.hello.vehicle.prediction.framework.hilt

import com.project.hello.vehicle.prediction.framework.internal.fps.FpsCounterManager
import com.project.hello.vehicle.prediction.framework.internal.fps.implementation.FpsCounterManagerEmpty
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
internal abstract class FpsCounterFragmentModule {

    @Binds
    abstract fun bindFpsCounterManager(debugImpl: FpsCounterManagerEmpty): FpsCounterManager
}