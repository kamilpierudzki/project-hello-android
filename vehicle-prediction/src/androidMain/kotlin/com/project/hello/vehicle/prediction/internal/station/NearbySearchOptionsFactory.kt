package com.project.hello.vehicle.prediction.internal.station

import android.location.Location

internal interface NearbySearchOptionsFactory {
    fun create(location: Location): Map<String, String>
}