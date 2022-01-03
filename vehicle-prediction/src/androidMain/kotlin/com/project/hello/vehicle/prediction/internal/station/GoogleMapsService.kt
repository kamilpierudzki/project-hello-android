package com.project.hello.vehicle.prediction.internal.station

import com.project.hello.vehicle.prediction.internal.station.model.NearbySearchAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

internal interface GoogleMapsService {
    @GET("maps/api/place/nearbysearch/json")
    fun nearbySearch(@QueryMap options: Map<String, String>): Call<NearbySearchAPI>
}