package com.project.hello.city.plan.framework.internal.datasource.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.hello.city.plan.domain.model.TransitAgency

@Entity
internal data class TransitAgencyEntity(
    val transitAgency: String,
    val lastUpdateFormatted: String,
    val trams: String,
    val buses: String,
    @PrimaryKey(autoGenerate = true) val cityPlanId: Int = 0
) {
    companion object {
        fun fromCityPlan(transitAgency: TransitAgency): TransitAgencyEntity {
            return TransitAgencyEntity(
                transitAgency = transitAgency.transitAgency,
                lastUpdateFormatted = transitAgency.lastUpdateFormatted,
                trams = TransitAgencyDatabaseConverters.linesToString(transitAgency.trams),
                buses = TransitAgencyDatabaseConverters.linesToString(transitAgency.buses),
            )
        }

        fun toCityPlan(transitAgencyEntity: TransitAgencyEntity) =
            TransitAgency(
                transitAgency = transitAgencyEntity.transitAgency,
                lastUpdateFormatted = transitAgencyEntity.lastUpdateFormatted,
                trams = TransitAgencyDatabaseConverters.stringToLines(transitAgencyEntity.trams),
                buses = TransitAgencyDatabaseConverters.stringToLines(transitAgencyEntity.buses),
            )
    }
}