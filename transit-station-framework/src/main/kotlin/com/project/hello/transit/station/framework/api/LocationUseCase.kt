package com.project.hello.transit.station.framework.api

import android.location.Location
import androidx.lifecycle.LiveData

interface LocationUseCase {
    val locationUpdates: LiveData<Location>
    fun startLocationUpdates()
    fun dispose()
}