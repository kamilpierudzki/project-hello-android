package com.project.hallo.city.plan.framework.internal

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hallo.city.plan.domain.model.SupportedCitiesData
import com.project.hallo.city.plan.domain.usecase.SupportedCitiesUseCase
import com.project.hallo.city.plan.framework.api.CityPickViewModel
import com.project.hallo.city.plan.framework.api.CitySelection
import com.project.hallo.city.plan.framework.api.FetchingCityStatus
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.framework.livedata.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CityPickViewModelImpl @Inject constructor(
    private val supportedCitiesUseCase: SupportedCitiesUseCase
) : ViewModel(), CityPickViewModel {

    override val fetchingCityStatus = MutableLiveData<Event<FetchingCityStatus>>()
    override val currentlySelectedCity = MutableLiveData<Event<CitySelection>>()

    init {
        fetchCurrentlySelectedCity()
        collectSupportedCities()
    }

    override fun selectCity(city: String) {
        fetchingCityStatus.postValue(Event(FetchingCityStatus.InProgress))
        // todo it is temporary
        Handler().postDelayed({
            fetchingCityStatus.postValue(Event(FetchingCityStatus.Success))
            currentlySelectedCity.postValue(Event(CitySelection.Selected(city)))
        }, 5_000L)
    }

    private fun fetchCurrentlySelectedCity() {
        Handler().postDelayed({
            currentlySelectedCity.postValue(Event(CitySelection.NotSelected))
        }, 2_000L)
    }

    private fun collectSupportedCities() {
        viewModelScope.launch {
            supportedCitiesUseCase.getSupportedCities()
                .collect { supportedCitiesResult ->
                    when (supportedCitiesResult) {
                        is Response.Success -> {
                            val data: SupportedCitiesData? = supportedCitiesResult.data
                            handleSupportedCities(data!!)
                        }
                        is Response.Error -> {
                            showErrorMessage(supportedCitiesResult.message)
                        }
                        is Response.Loading -> {
                            handleLoadingSupportedCities()
                        }
                    }
                }
        }
    }

    private fun handleSupportedCities(data: SupportedCitiesData) {
        //todo handle the list
    }

    private fun showErrorMessage(message: String?) {
        // todo show this message
    }

    private fun handleLoadingSupportedCities() {
        // todo
    }
}