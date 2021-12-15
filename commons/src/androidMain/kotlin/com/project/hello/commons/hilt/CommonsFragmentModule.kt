package com.project.hello.commons.hilt

import com.project.hello.commons.actionbar.ActionBarUpIndicatorVisibility
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