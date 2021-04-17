package com.project.hallo.city.plan.framework.internal

import androidx.lifecycle.ViewModel
import com.project.hallo.city.plan.framework.api.CityPickViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class CityPickViewModelImpl @Inject constructor() : ViewModel(), CityPickViewModel {

}