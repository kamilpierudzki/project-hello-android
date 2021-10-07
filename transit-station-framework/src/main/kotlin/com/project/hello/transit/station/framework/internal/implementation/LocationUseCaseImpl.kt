package com.project.hello.transit.station.framework.internal.implementation

import android.annotation.SuppressLint
import android.location.Location
import android.os.HandlerThread
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.project.hello.transit.station.framework.api.LocationUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
internal class LocationUseCaseImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationUseCase {

    private lateinit var handlerThread: HandlerThread
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let { lastLocation ->
                locationUpdates.postValue(lastLocation)
            }
        }
    }

    override val locationUpdates = MutableLiveData<Location>()

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        val locationRequest = LocationRequest.create()
        with(locationRequest) {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 30_000
        }

        handlerThread = HandlerThread("gps-position-thread")
        handlerThread.start()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            handlerThread.looper
        )
    }

    override fun dispose() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        handlerThread.quitSafely()
    }
}