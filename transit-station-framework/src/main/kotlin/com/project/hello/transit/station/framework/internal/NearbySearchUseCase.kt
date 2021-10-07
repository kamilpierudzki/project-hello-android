package com.project.hello.transit.station.framework.internal

import android.location.Location

internal interface NearbySearchUseCase {
    fun getNearbySearchResult(location: Location): NearbySearchAPI?
}