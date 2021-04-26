package com.project.hallo.city.plan.framework.api

import androidx.lifecycle.LiveData
import com.project.hallo.commons.framework.livedata.Event
import com.project.hallo.commons.framework.viewmodel.ExternalViewModel

interface CityPickViewModel : ExternalViewModel {
    val fetchingCityStatus: LiveData<Event<FetchingCityStatus>>
    val currentlySelectedCity: LiveData<Event<CitySelection>>
    val supportedCities: LiveData<FetchingSupportedCitiesStatus>

    fun selectCity(city: String)
    fun forceFetchSupportedCities()
}

sealed class FetchingSupportedCitiesStatus {
    object Loading : FetchingSupportedCitiesStatus()
    class Success(val supportedCities: List<String>) : FetchingSupportedCitiesStatus()
    class Error(val message: String) : FetchingSupportedCitiesStatus()
}

sealed class FetchingCityStatus {
    object Loading : FetchingCityStatus()
    object Success : FetchingCityStatus()
    object Error : FetchingCityStatus()
}

sealed class CitySelection {
    class Selected(val cityName: String) : CitySelection()
    object NotSelected : CitySelection()
}