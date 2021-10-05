package com.project.hello.transit.station.framework.api

import com.project.hello.transit.agency.domain.model.TransitAgencyStop
import kotlinx.coroutines.flow.Flow

interface TransitStationUseCase {
    fun execute(): Flow<TransitAgencyStop>
}