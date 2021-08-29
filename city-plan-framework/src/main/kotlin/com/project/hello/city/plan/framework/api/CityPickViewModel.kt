package com.project.hello.city.plan.framework.api

import androidx.lifecycle.LiveData
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.commons.framework.livedata.Event
import com.project.hello.commons.framework.viewmodel.ExternalViewModel

interface CityPickViewModel : ExternalViewModel {
    val currentlySelectedCityEvent: LiveData<Event<CitySelection>>
    val supportedCities: LiveData<Event<SupportedCitiesStatus>>
    val processing: LiveData<Boolean>
    val currentlySelectedCity: CityPlan?
}

internal interface InternalCityPickViewModel: ExternalViewModel {
    val currentlySelectedCityChangedEvent: LiveData<Event<CitySelection>>

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