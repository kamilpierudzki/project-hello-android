package com.project.hello.vehicle.prediction.internal.station

import android.location.Location
import com.project.hello.vehicle.prediction.internal.station.model.NearbySearchAPI

internal interface NearbySearchUseCase {
    fun getNearbySearchResult(location: Location): NearbySearchAPI?
}