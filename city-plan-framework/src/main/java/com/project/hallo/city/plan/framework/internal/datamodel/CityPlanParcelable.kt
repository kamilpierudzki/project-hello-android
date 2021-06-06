package com.project.hallo.city.plan.framework.internal.datamodel

import android.os.Parcelable
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.ICityPlan
import com.project.hallo.city.plan.domain.model.Line
import kotlinx.parcelize.Parcelize

@Parcelize
class CityPlanParcelable(
    override val city: String,
    private val tramsParcelable: List<LineParcelable>,
    private val busesParcelable: List<LineParcelable>
) : ICityPlan, Parcelable {

    override val trams: List<Line>
        get() = tramsParcelable.map {
            Line(number = it.number, destination = it.destination)
        }

    override val buses: List<Line>
        get() = busesParcelable.map {
            Line(number = it.number, destination = it.destination)
        }

    companion object {
        fun fromCityPlan(cityPlan: CityPlan): CityPlanParcelable {
            return CityPlanParcelable(
                city = cityPlan.city,
                tramsParcelable = cityPlan.trams.map {
                    LineParcelable(
                        number = it.number,
                        destination = it.destination
                    )
                },
                busesParcelable = cityPlan.buses.map {
                    LineParcelable(
                        number = it.number,
                        destination = it.destination
                    )
                }
            )
        }
    }
}

