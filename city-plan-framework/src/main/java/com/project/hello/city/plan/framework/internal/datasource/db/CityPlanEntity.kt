package com.project.hello.city.plan.framework.internal.datasource.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.hallo.city.plan.domain.model.CityPlan
import java.util.ArrayList

@Entity
internal data class CityPlanEntity(
    val city: String,
    val trams: String,
    val buses: String,
    @PrimaryKey(autoGenerate = true) val cityPlanId: Int = 0
) {
    companion object {
        fun fromCityPlan(cityPlan: CityPlan): CityPlanEntity {
            return CityPlanEntity(
                city = cityPlan.city,
                trams = CityDatabaseConverters.linesToString(cityPlan.trams),
                buses = CityDatabaseConverters.linesToString(cityPlan.buses),
            )
        }

        fun toCityPlan(cityPlanEntity: CityPlanEntity) =
            CityPlan(
                city = cityPlanEntity.city,
                trams = CityDatabaseConverters.stringToLines(cityPlanEntity.trams),
                buses = CityDatabaseConverters.stringToLines(cityPlanEntity.buses),
            )
    }
}