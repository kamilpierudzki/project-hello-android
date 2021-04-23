package com.project.hallo.city.plan.framework.hilt

import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.project.hallo.city.plan.framework.api.CityPickViewModel
import com.project.hallo.city.plan.framework.internal.CityPickViewModelImpl
import com.project.hallo.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class CityPlanActivityModule {

    @Provides
    @ViewModelProvider(ViewModelType.ACTIVITY)
    fun provideCityPickViewModelProvider(
        activity: FragmentActivity
    ) = ExternalViewModelProvider<CityPickViewModel> {
        activity.viewModels<CityPickViewModelImpl>().value
    }
}