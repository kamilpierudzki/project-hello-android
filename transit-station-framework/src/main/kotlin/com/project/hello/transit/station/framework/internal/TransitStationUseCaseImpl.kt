package com.project.hello.transit.station.framework.internal

import com.project.hello.transit.station.framework.api.TransitStationUseCase
import com.project.hello.transit.agency.domain.model.TransitAgencyStop
import kotlinx.coroutines.flow.Flow

internal class TransitStationUseCaseImpl: TransitStationUseCase {

    override fun execute(): Flow<TransitAgencyStop> {
        // todo gps position
        // todo google maps api request
        // todo process the request
        // todo emit a transit agency stop
        TODO("Not yet implemented")
    }
}