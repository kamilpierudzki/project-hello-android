package com.project.hello.legal.framework.api

import androidx.lifecycle.LiveData
import com.project.hello.commons.framework.livedata.Event
import com.project.hello.commons.framework.viewmodel.ExternalViewModel
import com.project.hello.legal.domain.model.LatestAvailableLegal

interface LegalViewModel : ExternalViewModel {
    val isLatestAvailableLegalAccepted: LiveData<Event<Boolean>>
    val latestAvailableLegal: LiveData<LatestAvailableLegal>
    val latestAvailableLegalSavedResult: LiveData<Event<LatestAvailableLegalResult>>

    fun onLatestAvailableLegalAccepted()
}

sealed class LatestAvailableLegalResult {
    object Success : LatestAvailableLegalResult()
    class Error(val message: String) : LatestAvailableLegalResult()
}