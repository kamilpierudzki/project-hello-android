package com.project.hallo.vehicle.prediction.framework.hilt

import androidx.fragment.app.Fragment
import com.project.hallo.vehicle.prediction.framework.internal.TextFromImageAnalyser
import com.project.hallo.vehicle.prediction.framework.internal.camera.CameraAnalysis
import com.project.hallo.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
internal abstract class VehiclePredictionFragmentModule {

    @Binds
    abstract fun bindDisposableImageAnalyzer(impl: TextFromImageAnalyser): DisposableImageAnalyzer

    companion object {

        @Provides
        fun provideCameraAnalysis(fragment: Fragment) = CameraAnalysis(fragment)
    }
}