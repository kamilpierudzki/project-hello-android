package com.project.hello.transit.agency.framework.api

import androidx.lifecycle.LiveData
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.commons.framework.livedata.Event
import com.project.hello.commons.framework.viewmodel.ExternalViewModel

interface TransitAgencyPickViewModel : ExternalViewModel {
    val currentlySelectedTransitAgencyEvent: LiveData<Event<TransitAgencySelection>>
    val supportedTransitAgencies: LiveData<Event<SupportedTransitAgenciesStatus>>
    val processing: LiveData<Boolean>
    val currentlySelectedTransitAgency: TransitAgency?
}

internal interface InternalTransitAgencyPickViewModel: ExternalViewModel {
    val currentlySelectedTransitAgencyChangedEvent: LiveData<Event<TransitAgencySelection>>

    fun selectTransitAgency(transitAgency: TransitAgency)
}

sealed class SupportedTransitAgenciesStatus {
    class Success(val supportedTransitAgencies: List<TransitAgency>) : SupportedTransitAgenciesStatus()
    class Error(val message: String) : SupportedTransitAgenciesStatus()
}

sealed class TransitAgencySelection {
    class Selected(val transitAgency: TransitAgency) : TransitAgencySelection()
    class NotSelected(val message: String) : TransitAgencySelection()
}