package com.project.hello.transit.agency.hilt

import com.project.hello.transit.agency.internal.ui.TransitAgencyPickerAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
internal class TransitAgencyFragmentModule {

    @Provides
    fun provideTransitAgencyPickerAdapter() = TransitAgencyPickerAdapter()
}