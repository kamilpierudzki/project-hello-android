package com.project.hallo.commons.framework.hilt

import com.project.hallo.commons.framework.actionbar.ActionBarUpIndicatorVisibility
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
internal class CommonsFragmentModule {

    @Provides
    fun provideActionBarUpIndicatorVisibility() = ActionBarUpIndicatorVisibility()
}