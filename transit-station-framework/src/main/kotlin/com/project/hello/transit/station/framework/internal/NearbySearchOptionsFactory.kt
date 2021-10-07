package com.project.hello.transit.station.framework.internal

import android.location.Location

internal interface NearbySearchOptionsFactory {
    fun create(location: Location): Map<String, String>
}