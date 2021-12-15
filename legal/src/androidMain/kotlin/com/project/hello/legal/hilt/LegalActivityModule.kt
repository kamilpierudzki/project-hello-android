package com.project.hello.legal.hilt

import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.project.hello.commons.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelType
import com.project.hello.legal.api.LegalViewModel
import com.project.hello.legal.internal.LegalViewModelImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal class LegalActivityModule {

    @Provides
    @ViewModelProvider(ViewModelType.ACTIVITY)
    fun provideLegalViewModelProvider(
        activity: FragmentActivity
    ) = ExternalViewModelProvider<LegalViewModel> {
        activity.viewModels<LegalViewModelImpl>().value
    }
}