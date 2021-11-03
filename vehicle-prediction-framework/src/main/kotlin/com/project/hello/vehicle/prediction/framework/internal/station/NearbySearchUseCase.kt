package com.project.hello.vehicle.prediction.framework.internal.station

import android.location.Location

internal interface NearbySearchUseCase {
    fun getNearbySearchResult(location: Location): NearbySearchAPI?
}