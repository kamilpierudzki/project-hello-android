package com.project.hello.city.plan.framework.internal

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.SupportedCitiesData
import com.project.hallo.city.plan.domain.usecase.CitySelectionUseCase
import com.project.hallo.city.plan.domain.usecase.SelectedCityUseCase
import com.project.hallo.city.plan.domain.usecase.SupportedCitiesUseCase
import com.project.hallo.commons.domain.data.Response
import com.project.hallo.commons.framework.hilt.IoDispatcher
import com.project.hallo.commons.framework.livedata.Event
import com.project.hello.city.plan.framework.api.CityPickViewModel
import com.project.hello.city.plan.framework.api.CitySelection
import com.project.hello.city.plan.framework.api.InternalCityPickViewModel
import com.project.hello.city.plan.framework.api.SupportedCitiesStatus
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
) : ViewModel(), CityPickViewModel, InternalCityPickViewModel {

    override val currentlySelectedCityEvent = MutableLiveData<Event<CitySelection>>()
    override val currentlySelectedCityChangedEvent = MutableLiveData<Event<CitySelection>>()
    override val currentlySelectedCity: CityPlan? get() {
        val event = currentlySelectedCityEvent.value?.content
        return if (event is CitySelection.Selected) {
            event.cityPlan
        } else {
            null
        }
    }
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
                        .collect()
                }
                .collect()
        }
    }

    override fun selectCity(city: CityPlan) {
        viewModelScope.launch {
            citySelectionUseCase.execute(city)
                .collect {
                    handleSelectedCityResult(it)
                }
        }
    }

    private fun handleSelectedCityResult(selectedCityResult: Response<CityPlan>) {
        when (selectedCityResult) {
            is Response.Error -> selectedCityFailed(selectedCityResult)
            is Response.Success -> selectedCitySucceeded(selectedCityResult)
            is Response.Loading -> processing.postValue(true)
        }
    }

    private fun selectedCityFailed(result: Response.Error<CityPlan>) {
        val selection: CitySelection = CitySelection.NotSelected(result.localisedErrorMessage)
        currentlySelectedCityEvent.postValue(Event(selection))
        currentlySelectedCityChangedEvent.postValue(Event(selection))
        processing.postValue(false)
    }

    private fun selectedCitySucceeded(result: Response.Success<CityPlan>) {
        val selection: CitySelection = CitySelection.Selected(result.successData)
        currentlySelectedCityEvent.postValue(Event(selection))
        currentlySelectedCityChangedEvent.postValue(Event(selection))
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
        val status = SupportedCitiesStatus.Success(data.supportedCities)
        this.supportedCities.postValue(Event(status))
        processing.postValue(false)
    }

    private fun fetchSupportedCitiesFailed(result: Response.Error<SupportedCitiesData>) {
        val status = SupportedCitiesStatus.Error(result.localisedErrorMessage)
        supportedCities.postValue(Event(status))
        processing.postValue(false)
    }
}