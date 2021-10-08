package com.project.hello.transit.station.framework.internal.implementation

import android.annotation.SuppressLint
import android.location.Location
import android.os.HandlerThread
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.project.hello.commons.framework.livedata.Event
import com.project.hello.transit.station.framework.api.LocationSettingsSatisfaction
import com.project.hello.transit.station.framework.api.LocationUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
internal class LocationUseCaseImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val settingsClient: SettingsClient,
) : LocationUseCase {

    private lateinit var handlerThread: HandlerThread
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let { lastLocation ->
                locationUpdates.postValue(lastLocation)
            }
        }
    }

    private val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 30_000
    }

    override val locationUpdates = MutableLiveData<Location>()
    override val locationSettingsSatisfactionEvent =
        MutableLiveData<Event<LocationSettingsSatisfaction>>()

    init {
        setupLocationSettingsObserver()
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
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
        if (this::handlerThread.isInitialized) {
            handlerThread.quitSafely()
        }
    }

    private fun setupLocationSettingsObserver() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val task = settingsClient.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                val result = LocationSettingsSatisfaction.NotSatisfied(exception)
                locationSettingsSatisfactionEvent.postValue(Event(result))
            }
        }
        task.addOnSuccessListener {
            val result = LocationSettingsSatisfaction.Satisfied
            locationSettingsSatisfactionEvent.postValue(Event(result))
        }
    }
}