package com.project.hello.transit.station.framework.internal

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

internal interface GoogleMapsService {
    @GET("maps/api/place/nearbysearch/json")
    fun nearbySearch(@QueryMap options: Map<String, String>): Call<NearbySearchAPI>
}