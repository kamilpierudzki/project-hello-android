package com.project.halo.vehicle.prediction.framework.hilt

import androidx.fragment.app.Fragment
import com.project.halo.commons.viewmodel.ExternalViewModelProvider
import com.project.halo.commons.viewmodel.ViewModelProvider
import com.project.halo.commons.viewmodel.ViewModelType
import com.project.halo.vehicle.prediction.framework.internal.PredictionViewModelImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import androidx.fragment.app.viewModels
import com.project.halo.vehicle.prediction.framework.api.PredictionViewModel
import com.project.halo.vehicle.prediction.framework.internal.TextFromImageAnalyser
import com.project.halo.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import dagger.Binds

@Module
@InstallIn(FragmentComponent::class)
internal abstract class VehiclePredictionFragmentModule {

    @Binds
    abstract fun bindDisposableImageAnalyzer(impl: TextFromImageAnalyser): DisposableImageAnalyzer

    companion object {

        @Provides
        @ViewModelProvider(ViewModelType.FRAGMENT)
        fun provideUpNextViewModelProvider(
            fragment: Fragment
        ) = ExternalViewModelProvider<PredictionViewModel> {
            fragment.viewModels<PredictionViewModelImpl>().value
        }
    }
}