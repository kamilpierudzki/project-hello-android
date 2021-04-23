package com.project.hallo.city.plan.framework.api

import androidx.lifecycle.LiveData
import com.project.hallo.commons.framework.livedata.Event
import com.project.hallo.commons.framework.viewmodel.ExternalViewModel

interface CityPickViewModel : ExternalViewModel {
    val fetchingCityStatus: LiveData<Event<FetchingCityStatus>>
    val currentlySelectedCity: LiveData<Event<CitySelection>>

    fun selectCity(city: String)
}

sealed class FetchingCityStatus {
    object InProgress : FetchingCityStatus()
    object Success : FetchingCityStatus()
    object Error : FetchingCityStatus()
}

sealed class CitySelection {
    class Selected(val cityName: String) : CitySelection()
    object NotSelected : CitySelection()
}