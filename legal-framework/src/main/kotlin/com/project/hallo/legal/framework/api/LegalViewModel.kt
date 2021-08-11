package com.project.hallo.legal.framework.api

import androidx.lifecycle.LiveData
import com.project.hallo.commons.framework.livedata.Event
import com.project.hello.legal.domain.model.LatestAvailableLegal

interface LegalViewModel {
    val isLatestAvailableLegalAccepted: LiveData<Event<Boolean>>
    val latestAvailableLegal: LiveData<LatestAvailableLegal>
    val latestAvailableLegalSavedResult: LiveData<Event<LatestAvailableLegalResult>>

    fun onLatestAvailableLegalAccepted()
}

sealed class LatestAvailableLegalResult {
    object Success : LatestAvailableLegalResult()
    class Error(val message: String) : LatestAvailableLegalResult()
}