package com.project.hello.transit.station.framework.api

import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.project.hello.commons.framework.livedata.Event

interface LocationUseCase {
    val locationUpdates: LiveData<Location>
    val locationSettingsSatisfactionEvent: LiveData<Event<LocationSettingsSatisfaction>>
    fun startLocationUpdates()
    fun dispose()
}

sealed class LocationSettingsSatisfaction {
    object Satisfied : LocationSettingsSatisfaction()
    class NotSatisfied(val exception: ResolvableApiException) : LocationSettingsSatisfaction()
}