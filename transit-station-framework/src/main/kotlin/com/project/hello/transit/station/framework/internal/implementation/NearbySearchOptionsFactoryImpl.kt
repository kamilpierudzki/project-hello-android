package com.project.hello.transit.station.framework.internal.implementation

import android.location.Location
import com.project.hello.transit.station.framework.BuildConfig
import com.project.hello.transit.station.framework.internal.NearbySearchOptionsFactory
import com.project.hello.transit.station.framework.internal.NearbySearchValues.ParamName
import com.project.hello.transit.station.framework.internal.NearbySearchValues.ParamValue
import javax.inject.Inject


internal class NearbySearchOptionsFactoryImpl @Inject constructor() : NearbySearchOptionsFactory {
    override fun create(location: Location) = mapOf(
        ParamName.LOCATION_NAME to /*"${location.latitude},${location.longitude}",*/"52.394737,16.949834",
        ParamName.RADIUS_NAME to ParamValue.RADIUS_VALUE,
        ParamName.PLACE_TYPE_NAME to ParamValue.PLACE_TYPE_VALUE,
        ParamName.KEY_NAME to BuildConfig.GOOGLE_MAPS_API_KEY
    )
}