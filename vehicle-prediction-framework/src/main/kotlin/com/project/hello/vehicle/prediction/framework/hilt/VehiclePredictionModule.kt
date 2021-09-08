package com.project.hello.vehicle.prediction.framework.hilt

import com.project.hello.vehicle.prediction.framework.internal.PredictionConsoleLogger
import com.project.hello.vehicle.prediction.framework.internal.implementation.PredictionConsoleLoggerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class VehiclePredictionModule {

    @Binds
    abstract fun bindPredictionConsoleLogger(impl: PredictionConsoleLoggerImpl):
            PredictionConsoleLogger
}