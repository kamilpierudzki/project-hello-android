package com.project.hallo.city.plan.framework.api

import androidx.lifecycle.LiveData
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.commons.framework.livedata.Event
import com.project.hallo.commons.framework.viewmodel.ExternalViewModel

interface CityPickViewModel : ExternalViewModel {
    val currentlySelectedCityEvent: LiveData<Event<CitySelection>>
    val currentlySelectedCity: LiveData<CityPlan?>
    val supportedCities: LiveData<Event<SupportedCitiesStatus>>
    val processing: LiveData<Boolean>

    fun selectCity(city: CityPlan)
}

sealed class SupportedCitiesStatus {
    class Success(val supportedCities: List<CityPlan>) : SupportedCitiesStatus()
    class Error(val message: String) : SupportedCitiesStatus()
}

sealed class CitySelection {
    class Selected(val cityPlan: CityPlan) : CitySelection()
    class NotSelected(val message: String) : CitySelection()
}