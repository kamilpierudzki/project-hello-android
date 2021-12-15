package com.project.hello.vehicle.prediction.hilt

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.core.content.ContextCompat
import com.project.hello.vehicle.prediction.internal.logger.PredictionConsoleLogger
import com.project.hello.vehicle.prediction.internal.logger.implementation.PredictionConsoleLoggerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class VehiclePredictionModule {

    @Binds
    abstract fun bindPredictionConsoleLogger(impl: PredictionConsoleLoggerImpl):
            PredictionConsoleLogger

    companion object {

        @Provides
        @Singleton
        fun provideCameraManager(@ApplicationContext appContext: Context): CameraManager =
            ContextCompat.getSystemService(appContext, CameraManager::class.java)!!
    }
}