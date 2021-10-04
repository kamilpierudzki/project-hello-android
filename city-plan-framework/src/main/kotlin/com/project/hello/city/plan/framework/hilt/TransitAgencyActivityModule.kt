package com.project.hello.city.plan.framework.hilt

import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.project.hello.city.plan.framework.api.InternalTransitAgencyPickViewModel
import com.project.hello.city.plan.framework.api.TransitAgencyPickViewModel
import com.project.hello.city.plan.framework.internal.TransitAgencyPickViewModelImpl
import com.project.hello.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelType
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