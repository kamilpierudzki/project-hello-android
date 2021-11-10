package com.project.hello.vehicle.prediction.framework.internal.station.implementation

import android.location.Location
import com.project.hello.vehicle.prediction.framework.BuildConfig
import com.project.hello.vehicle.prediction.framework.internal.station.NearbySearchOptionsFactory
import com.project.hello.vehicle.prediction.framework.internal.station.NearbySearchValues.ParamName
import com.project.hello.vehicle.prediction.framework.internal.station.NearbySearchValues.ParamValue
import javax.inject.Inject


internal class NearbySearchOptionsFactoryImpl @Inject constructor() : NearbySearchOptionsFactory {

    override fun create(location: Location) = mapOf(
        ParamName.LOCATION_NAME to "${location.latitude},${location.longitude}",
        ParamName.RADIUS_NAME to ParamValue.RADIUS_VALUE,
        ParamName.PLACE_TYPE_NAME to ParamValue.PLACE_TYPE_VALUE,
        ParamName.KEY_NAME to BuildConfig.GOOGLE_MAPS_API_KEY
    )
}