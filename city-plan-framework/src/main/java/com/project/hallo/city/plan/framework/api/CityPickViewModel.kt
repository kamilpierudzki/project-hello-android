package com.project.hallo.city.plan.framework.api

import androidx.lifecycle.LiveData
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.commons.framework.livedata.Event
import com.project.hallo.commons.framework.viewmodel.ExternalViewModel

interface CityPickViewModel : ExternalViewModel {
    val currentlySelectedCity: LiveData<Event<CitySelection>>
    val supportedCities: LiveData<Event<SupportedCitiesStatus>>
    val processing: LiveData<Boolean>

    fun selectCity(city: CityPlan)
    fun forceFetchSupportedCities()
}

sealed class SupportedCitiesStatus {
    class Success(val supportedCities: List<City>) : SupportedCitiesStatus() {
        data class City(val cityPlan: CityPlan, val currentlySelected: Boolean)
    }

    class Error(val message: String) : SupportedCitiesStatus()
}

sealed class CitySelection {
    class Selected(val cityPlan: CityPlan) : CitySelection()
    object NotSelected : CitySelection()
}