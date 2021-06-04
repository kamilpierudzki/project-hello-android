package com.project.hallo.city.plan.framework.internal

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.SupportedCitiesData
import com.project.hallo.city.plan.domain.usecase.CitySelectionUseCase
import com.project.hallo.city.plan.domain.usecase.SelectedCityUseCase
import com.project.hallo.city.plan.domain.usecase.SupportedCitiesUseCase
import com.project.hallo.city.plan.framework.R
import com.project.hallo.city.plan.framework.api.CityPickViewModel
import com.project.hallo.city.plan.framework.api.CitySelection
import com.project.hallo.city.plan.framework.api.SupportedCitiesStatus
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.framework.hilt.IoDispatcher
import com.project.hallo.commons.framework.livedata.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
internal class CityPickViewModelImpl @Inject constructor(
    private val supportedCitiesUseCase: SupportedCitiesUseCase,
    private val citySelectionUseCase: CitySelectionUseCase,
    private val selectedCityUseCase: SelectedCityUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), CityPickViewModel {

    override val currentlySelectedCity = MutableLiveData<Event<CitySelection>>()
    override val supportedCities = MutableLiveData<Event<SupportedCitiesStatus>>()
    override val processing = MutableLiveData<Boolean>()

    init {
        fetchData()
    }

    @VisibleForTesting
    fun fetchData() {
        viewModelScope.launch(ioDispatcher) {
            selectedCityUseCase.execute()
                .onEach {
                    handleSelectedCityResult(it)
                }
                .onCompletion {
                    supportedCitiesUseCase.execute()
                        .onEach {
                            handleSupportedCitiesResult(it)
                        }
                        .collect { }
                }
                .collect()
        }
    }

    override fun selectCity(city: CityPlan) {
        viewModelScope.launch {
            citySelectionUseCase.execute(city)
                .collect { cityPlanResult ->
                    when (cityPlanResult) {
                        is Response.Success -> selectedCitySucceeded(cityPlanResult)
                        is Response.Error -> selectedCityFailed()
                        is Response.Loading -> processing.postValue(true)
                    }
                }
        }
    }

    private fun handleSelectedCityResult(selectedCityResult: Response<CityPlan>) {
        when (selectedCityResult) {
            is Response.Error -> selectedCityFailed()
            is Response.Success -> selectedCitySucceeded(selectedCityResult)
            is Response.Loading -> processing.postValue(true)
        }
    }

    private fun selectedCityFailed() {
        val selection = CitySelection.NotSelected(R.string.city_selection_error)
        currentlySelectedCity.postValue(Event(selection))
        processing.postValue(false)
    }

    private fun selectedCitySucceeded(result: Response.Success<CityPlan>) {
        val selection = CitySelection.Selected(result.successData)
        currentlySelectedCity.postValue(Event(selection))
        processing.postValue(false)
    }

    private fun handleSupportedCitiesResult(supportedCitiesResult: Response<SupportedCitiesData>) {
        when (supportedCitiesResult) {
            is Response.Success -> fetchSupportedCitiesSucceeded(supportedCitiesResult)
            is Response.Error -> fetchSupportedCitiesFailed(supportedCitiesResult)
            is Response.Loading -> processing.postValue(true)
        }
    }

    private fun fetchSupportedCitiesSucceeded(result: Response.Success<SupportedCitiesData>) {
        val data: SupportedCitiesData = result.successData
        val currentlySelectedCity: CitySelection.Selected? =
            currentlySelectedCity.value?.content as? CitySelection.Selected
        val supportedCities = data.supportedCities.map {
            val isCurrentlySelected = currentlySelectedCity?.cityPlan == it
            SupportedCitiesStatus.Success.City(it, isCurrentlySelected)
        }
        val status = SupportedCitiesStatus.Success(supportedCities)
        this.supportedCities.postValue(Event(status))
        processing.postValue(false)
    }

    private fun fetchSupportedCitiesFailed(result: Response.Error<SupportedCitiesData>) {
        val status = SupportedCitiesStatus.Error(result.errorMessage)
        supportedCities.postValue(Event(status))
        processing.postValue(false)
    }
}