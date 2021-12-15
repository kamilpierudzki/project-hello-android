package com.project.hello.vehicle.prediction.internal.station

import android.location.Location
import com.project.hello.transit.agency.model.Stop
import com.project.hello.transit.agency.model.TransitAgency
import kotlinx.coroutines.flow.Flow

internal interface TransitStationUseCase {
    fun execute(data: TransitStationData): Flow<TransitStationResult>
}

internal data class TransitStationData(
    val location: Location,
    val selectedTransitAgency: TransitAgency
)

internal data class TransitStationResult(
    val tramStops: List<Stop>,
    val busStops: List<Stop>,
) {
    companion object {
        val EMPTY = TransitStationResult(emptyList(), emptyList())
    }
}