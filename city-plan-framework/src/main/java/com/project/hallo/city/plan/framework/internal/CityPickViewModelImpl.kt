package com.project.hallo.city.plan.framework.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.SupportedCitiesData
import com.project.hallo.city.plan.domain.usecase.CityUseCase
import com.project.hallo.city.plan.domain.usecase.SupportedCitiesUseCase
import com.project.hallo.city.plan.framework.api.CityPickViewModel
import com.project.hallo.city.plan.framework.api.CitySelection
import com.project.hallo.city.plan.framework.api.FetchingCityStatus
import com.project.hallo.city.plan.framework.api.FetchingSupportedCitiesStatus
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.framework.livedata.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CityPickViewModelImpl @Inject constructor(
    private val supportedCitiesUseCase: SupportedCitiesUseCase,
    private val cityUseCase: CityUseCase
) : ViewModel(), CityPickViewModel {

    override val fetchingCityStatus = MutableLiveData<Event<FetchingCityStatus>>()
    override val currentlySelectedCity = MutableLiveData<Event<CitySelection>>()
    override val supportedCities = MutableLiveData<FetchingSupportedCitiesStatus>()

    init {
        fetchSupportedCities()
    }

    override fun selectCity(city: String) {
        viewModelScope.launch {
            cityUseCase.execute(city)
                .collect { cityPlanResult ->
                    when (cityPlanResult) {
                        is Response.Error -> fetchingCityFailed(cityPlanResult)
                        is Response.Loading -> fetchingCityLoading()
                        is Response.Success -> fetchingCitySucceeded()
                    }
                }
        }
    }

    override fun forceFetchSupportedCities() {
        fetchSupportedCities()
    }

    private fun fetchingCitySucceeded() {
        fetchingCityStatus.postValue(Event(FetchingCityStatus.Success))
    }

    private fun fetchingCityFailed(result: Response.Error<CityPlan>) {
        showErrorMessage(result.message)
    }

    private fun fetchingCityLoading() {
        fetchingCityStatus.postValue(Event(FetchingCityStatus.Loading))
    }

    private fun fetchSupportedCities() {
        viewModelScope.launch {
            supportedCitiesUseCase.execute()
                .collect { supportedCitiesResult ->
                    when (supportedCitiesResult) {
                        is Response.Success -> fetchSupportedCitiesSucceeded(supportedCitiesResult)
                        is Response.Error -> fetchSupportedCitiesFailed(supportedCitiesResult)
                        is Response.Loading -> fetchSupportedCitiesLoading()
                    }
                }
        }
    }

    private fun fetchSupportedCitiesSucceeded(result: Response.Success<SupportedCitiesData>) {
        val data: SupportedCitiesData = result.data!!
        val status = FetchingSupportedCitiesStatus.Success(data.supportedCities)
        supportedCities.postValue(status)
    }

    private fun fetchSupportedCitiesFailed(result: Response.Error<SupportedCitiesData>) {
        showErrorMessage(result.message)
    }

    private fun showErrorMessage(message: String?) {
        val status = FetchingSupportedCitiesStatus.Error(message ?: "")
        supportedCities.postValue(status)
    }

    private fun fetchSupportedCitiesLoading() {
        val status = FetchingSupportedCitiesStatus.Loading
        supportedCities.postValue(status)
    }
}