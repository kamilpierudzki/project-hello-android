package com.project.hello.welcome.framework.hilt

import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.project.hello.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelType
import com.project.hello.welcome.framework.api.WelcomeViewModel
import com.project.hello.welcome.framework.internal.WelcomeViewModelImpl
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