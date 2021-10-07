package com.project.hello.transit.station.framework.internal.implementation

import android.location.Location
import com.project.hello.transit.station.framework.internal.GoogleMapsService
import com.project.hello.transit.station.framework.internal.NearbySearchOptionsFactory
import com.project.hello.transit.station.framework.internal.NearbySearchUseCase
import com.project.hello.transit.station.framework.internal.NearbySearchAPI
import retrofit2.Response
import javax.inject.Inject

internal class NearbySearchUseCaseImpl @Inject constructor(
    private val googleMapsService: GoogleMapsService,
    private val nearbySearchOptionsFactory: NearbySearchOptionsFactory,
) : NearbySearchUseCase {

    override fun getNearbySearchResult(location: Location): NearbySearchAPI? {
        val options = nearbySearchOptionsFactory.create(location)
        val response: Response<NearbySearchAPI> = googleMapsService.nearbySearch(options).execute()
        return response.body()
    }
}