package com.project.hello.transit.agency.api

import androidx.lifecycle.LiveData
import com.project.hello.commons.livedata.Event
import com.project.hello.commons.viewmodel.ExternalViewModel
import com.project.hello.transit.agency.model.TransitAgency

interface TransitAgencyPickViewModel : ExternalViewModel {
    val currentlySelectedTransitAgencyEvent: LiveData<Event<TransitAgencySelection>>
    val supportedTransitAgencies: LiveData<Event<SupportedTransitAgenciesStatus>>
    val processing: LiveData<Boolean>
    val currentlySelectedTransitAgency: TransitAgency?
}

internal interface InternalTransitAgencyPickViewModel : ExternalViewModel {
    val currentlySelectedTransitAgencyChangedEvent: LiveData<Event<TransitAgencySelection>>

    fun selectTransitAgency(transitAgency: TransitAgency)
}

sealed class SupportedTransitAgenciesStatus {
    class Success(val supportedTransitAgencies: List<TransitAgency>) :
        SupportedTransitAgenciesStatus()

    class Error(val message: String) : SupportedTransitAgenciesStatus()
}

sealed class TransitAgencySelection {
    class Selected(val transitAgency: TransitAgency) : TransitAgencySelection()
    class NotSelected(val message: String) : TransitAgencySelection()
}