package com.project.hello.transit.agency.framework.internal.repository.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.hello.transit.agency.domain.model.TransitAgency

@Entity
internal data class TransitAgencyEntity(
    val transitAgency: String,
    val lastUpdateFormatted: String,
    val tramLines: String,
    val busLines: String,
    val tramStations: String,
    val busStations: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    companion object {
        fun fromTransitAgency(transitAgency: TransitAgency): TransitAgencyEntity {
            return TransitAgencyEntity(
                transitAgency = transitAgency.transitAgency,
                lastUpdateFormatted = transitAgency.lastUpdateFormatted,
                tramLines = TransitAgencyDatabaseConverters.linesToString(transitAgency.tramLines),
                busLines = TransitAgencyDatabaseConverters.linesToString(transitAgency.busLines),
                tramStations = TransitAgencyDatabaseConverters.stationsToString(transitAgency.tramStops),
                busStations = TransitAgencyDatabaseConverters.stationsToString(transitAgency.busStops),
            )
        }

        fun toTransitAgency(transitAgencyEntity: TransitAgencyEntity) =
            TransitAgency(
                transitAgency = transitAgencyEntity.transitAgency,
                lastUpdateFormatted = transitAgencyEntity.lastUpdateFormatted,
                tramLines = TransitAgencyDatabaseConverters.stringToLines(transitAgencyEntity.tramLines),
                busLines = TransitAgencyDatabaseConverters.stringToLines(transitAgencyEntity.busLines),
                tramStops = TransitAgencyDatabaseConverters.stringToStops(transitAgencyEntity.tramStations),
                busStops = TransitAgencyDatabaseConverters.stringToStops(transitAgencyEntity.busStations),
            )
    }
}