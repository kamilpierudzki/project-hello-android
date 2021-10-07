package com.project.hello.transit.station.framework.api

import android.location.Location
import com.project.hello.transit.agency.domain.model.Stop
import com.project.hello.transit.agency.domain.model.TransitAgency
import kotlinx.coroutines.flow.Flow

interface TransitStationUseCase {
    fun execute(data: TransitStationData): Flow<TransitStationResult>
}

data class TransitStationData(
    val location: Location,
    val selectedTransitAgency: TransitAgency
)

data class TransitStationResult(
    val tramStops: List<Stop>,
    val busStops: List<Stop>,
) {
    companion object {
        val EMPTY = TransitStationResult(emptyList(), emptyList())
    }
}