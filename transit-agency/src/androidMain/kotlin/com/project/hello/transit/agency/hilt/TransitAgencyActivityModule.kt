package com.project.hello.transit.agency.hilt

import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.project.hello.commons.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelType
import com.project.hello.transit.agency.api.InternalTransitAgencyPickViewModel
import com.project.hello.transit.agency.api.TransitAgencyPickViewModel
import com.project.hello.transit.agency.internal.TransitAgencyPickViewModelImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@FlowPreview
@Module
@InstallIn(ActivityComponent::class)
internal class TransitAgencyActivityModule {

    @Provides
    @ViewModelProvider(ViewModelType.ACTIVITY)
    fun provideTransitAgencyPickViewModelProvider(
        activity: FragmentActivity
    ) = ExternalViewModelProvider<TransitAgencyPickViewModel> {
        activity.viewModels<TransitAgencyPickViewModelImpl>().value
    }

    @Provides
    @ViewModelProvider(ViewModelType.ACTIVITY)
    fun provideInternalTransitAgencyPickViewModelProvider(
        activity: FragmentActivity
    ) = ExternalViewModelProvider<InternalTransitAgencyPickViewModel> {
        activity.viewModels<TransitAgencyPickViewModelImpl>().value
    }
}