package com.project.hello.welcome.hilt

import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.project.hello.commons.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelType
import com.project.hello.welcome.api.WelcomeViewModel
import com.project.hello.welcome.internal.WelcomeViewModelImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal class WelcomeActivityModule {

    @Provides
    @ViewModelProvider(ViewModelType.ACTIVITY)
    fun provideLegalViewModelProvider(
        activity: FragmentActivity
    ) = ExternalViewModelProvider<WelcomeViewModel> {
        activity.viewModels<WelcomeViewModelImpl>().value
    }
}