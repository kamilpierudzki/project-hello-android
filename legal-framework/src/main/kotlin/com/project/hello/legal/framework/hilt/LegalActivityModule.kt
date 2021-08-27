package com.project.hello.legal.framework.hilt

import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.project.hello.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelType
import com.project.hello.legal.framework.api.LegalViewModel
import com.project.hello.legal.framework.internal.LegalViewModelImpl
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