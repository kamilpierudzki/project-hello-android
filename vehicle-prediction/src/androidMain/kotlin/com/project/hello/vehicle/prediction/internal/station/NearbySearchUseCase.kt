package com.project.hello.vehicle.prediction.internal.station

import android.location.Location

internal interface NearbySearchUseCase {
    fun getNearbySearchResult(location: Location): NearbySearchAPI?
}