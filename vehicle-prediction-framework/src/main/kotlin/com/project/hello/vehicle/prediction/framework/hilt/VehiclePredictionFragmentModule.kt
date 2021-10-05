package com.project.hello.vehicle.prediction.framework.hilt

import androidx.fragment.app.Fragment
import com.project.hello.vehicle.prediction.framework.internal.ui.TextFromImageAnalyser
import com.project.hello.vehicle.prediction.framework.internal.camera.CameraAnalysis
import com.project.hello.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
internal abstract class VehiclePredictionFragmentModule {

    @Binds
    abstract fun bindDisposableImageAnalyzer(impl: TextFromImageAnalyser): DisposableImageAnalyzer

    companion object {

        @Provides
        @FragmentScoped
        fun provideCameraAnalysis(fragment: Fragment) = CameraAnalysis(fragment)
    }
}