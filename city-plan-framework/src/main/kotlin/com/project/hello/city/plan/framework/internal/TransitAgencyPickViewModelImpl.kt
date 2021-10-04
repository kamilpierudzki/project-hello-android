package com.project.hello.city.plan.framework.internal

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.hello.transit.agency.domain.model.SupportedTransitAgenciesData
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.city.plan.framework.api.InternalTransitAgencyPickViewModel
import com.project.hello.city.plan.framework.api.SupportedTransitAgenciesStatus
import com.project.hello.city.plan.framework.api.TransitAgencyPickViewModel
import com.project.hello.city.plan.framework.api.TransitAgencySelection
import com.project.hello.city.plan.framework.internal.usecase.SelectedTransitAgencyUseCase
import com.project.hello.city.plan.framework.internal.usecase.SupportedTransitAgenciesUseCase
import com.project.hello.city.plan.framework.internal.usecase.TransitAgencySelectionUseCase
import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.framework.hilt.IoDispatcher
import com.project.hello.commons.framework.livedata.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
@FlowPreview
@HiltViewModel
internal class TransitAgencyPickViewModelImpl @Inject constructor(
    private val supportedTransitAgenciesUseCase: SupportedTransitAgenciesUseCase,
    private val transitAgencySelectionUseCase: TransitAgencySelectionUseCase,
    private val selectedTransitAgencyUseCase: SelectedTransitAgencyUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), TransitAgencyPickViewModel, InternalTransitAgencyPickViewModel {

    override val currentlySelectedTransitAgencyEvent = MutableLiveData<Event<TransitAgencySelection>>()
    override val currentlySelectedTransitAgencyChangedEvent = MutableLiveData<Event<TransitAgencySelection>>()
    override val currentlySelectedCity: TransitAgency?
        get() {
            val event = currentlySelectedTransitAgencyEvent.value?.content
            return if (event is TransitAgencySelection.Selected) {
                event.transitAgency
            } else {
                null
            }
        }
    override val supportedTransitAgencies = MutableLiveData<Event<SupportedTransitAgenciesStatus>>()
    override val processing = MutableLiveData<Boolean>()

    init {
        fetchData()
    }

    @VisibleForTesting
    fun fetchData() {
        viewModelScope.launch(ioDispatcher) {
            selectedTransitAgencyUseCase.execute()
                .onEach {
                    handleSelectedCityResult(it)
                }
                .onCompletion {
                    supportedTransitAgenciesUseCase.execute()
                        .onEach {
                            handleSupportedCitiesResult(it)
                        }
                        .collect()
                }
                .collect()
        }
    }

    override fun selectTransitAgency(transitAgency: TransitAgency) {
        viewModelScope.launch {
            transitAgencySelectionUseCase.execute(transitAgency)
                .collect {
                    handleSelectedCityResult(it)
                }
        }
    }

    private fun handleSelectedCityResult(selectedCityResult: Response<TransitAgency>) {
        when (selectedCityResult) {
            is Response.Error -> selectedCityFailed(selectedCityResult)
            is Response.Success -> selectedCitySucceeded(selectedCityResult)
            is Response.Loading -> processing.postValue(true)
        }
    }

    private fun selectedCityFailed(result: Response.Error<TransitAgency>) {
        val selection: TransitAgencySelection = TransitAgencySelection.NotSelected(result.localisedErrorMessage)
        currentlySelectedTransitAgencyEvent.postValue(Event(selection))
        currentlySelectedTransitAgencyChangedEvent.postValue(Event(selection))
        processing.postValue(false)
    }

    private fun selectedCitySucceeded(result: Response.Success<TransitAgency>) {
        val selection: TransitAgencySelection = TransitAgencySelection.Selected(result.successData)
        currentlySelectedTransitAgencyEvent.postValue(Event(selection))
        currentlySelectedTransitAgencyChangedEvent.postValue(Event(selection))
        processing.postValue(false)
    }

    private fun handleSupportedCitiesResult(supportedTransitAgenciesResult: Response<SupportedTransitAgenciesData>) {
        when (supportedTransitAgenciesResult) {
            is Response.Success -> fetchSupportedCitiesSucceeded(supportedTransitAgenciesResult)
            is Response.Error -> fetchSupportedCitiesFailed(supportedTransitAgenciesResult)
            is Response.Loading -> processing.postValue(true)
        }
    }

    private fun fetchSupportedCitiesSucceeded(result: Response.Success<SupportedTransitAgenciesData>) {
        val data: SupportedTransitAgenciesData = result.successData
        val status = SupportedTransitAgenciesStatus.Success(data.supportedTransitAgencies)
        this.supportedTransitAgencies.postValue(Event(status))
        processing.postValue(false)
    }

    private fun fetchSupportedCitiesFailed(result: Response.Error<SupportedTransitAgenciesData>) {
        val status = SupportedTransitAgenciesStatus.Error(result.localisedErrorMessage)
        supportedTransitAgencies.postValue(Event(status))
        processing.postValue(false)
    }
}