package com.project.hello.vehicle.prediction.framework.internal.station

import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.project.hello.commons.framework.livedata.Event

internal interface LocationUseCase {
    val locationUpdates: LiveData<Location>
    val locationSettingsSatisfactionEvent: LiveData<Event<LocationSettingsSatisfaction>>
    fun startLocationUpdates()
    fun dispose()
}

internal sealed class LocationSettingsSatisfaction {
    object Satisfied : LocationSettingsSatisfaction()
    class NotSatisfied(val exception: ResolvableApiException) : LocationSettingsSatisfaction()
}