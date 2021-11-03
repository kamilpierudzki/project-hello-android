package com.project.hello.vehicle.prediction.framework.internal.station.implementation

import android.location.Location
import com.project.hello.vehicle.prediction.framework.internal.station.GoogleMapsService
import com.project.hello.vehicle.prediction.framework.internal.station.NearbySearchAPI
import com.project.hello.vehicle.prediction.framework.internal.station.NearbySearchOptionsFactory
import com.project.hello.vehicle.prediction.framework.internal.station.NearbySearchUseCase
import retrofit2.Response
import javax.inject.Inject

internal class NearbySearchUseCaseImpl @Inject constructor(
    private val googleMapsService: GoogleMapsService,
    private val nearbySearchOptionsFactory: NearbySearchOptionsFactory,
) : NearbySearchUseCase {

    override fun getNearbySearchResult(location: Location): NearbySearchAPI? {
        return try {
            val options = nearbySearchOptionsFactory.create(location)
            val response: Response<NearbySearchAPI> =
                googleMapsService.nearbySearch(options).execute()
            response.body()
        } catch (t: Throwable) {
            null
        }
    }
}