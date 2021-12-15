package com.project.hello.vehicle.prediction.hilt

import androidx.fragment.app.Fragment
import com.project.hello.vehicle.prediction.internal.camera.CameraAnalysis
import com.project.hello.vehicle.prediction.internal.characters.ResourceCountryCharacters
import com.project.hello.vehicle.prediction.internal.characters.implementation.ResourceCountryCharactersImpl
import com.project.hello.vehicle.prediction.internal.textrecognition.DisposableImageAnalyzer
import com.project.hello.vehicle.prediction.internal.ui.TextFromImageAnalyser
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
        fun provideCameraAnalysis(fragment: Fragment) = CameraAnalysis(fragment)

        @Provides
        fun bindResourceCountryCharacters(fragment: Fragment): ResourceCountryCharacters =
            ResourceCountryCharactersImpl(fragment.resources)
    }
}