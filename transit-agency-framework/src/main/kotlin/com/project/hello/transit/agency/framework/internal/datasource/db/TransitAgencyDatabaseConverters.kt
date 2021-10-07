package com.project.hello.transit.agency.framework.internal.datasource.db

import com.google.gson.Gson
import com.project.hello.transit.agency.domain.model.Line
import com.project.hello.transit.agency.domain.model.Stop

internal object TransitAgencyDatabaseConverters {

    fun linesToString(lines: List<Line>): String = Gson().toJson(lines)

    fun stringToLines(value: String?): List<Line> =
        Gson().fromJson(value, Array<Line>::class.java).toList()

    fun stationsToString(stations: List<Stop>): String = Gson().toJson(stations)

    fun stringToStops(value: String): List<Stop> =
        Gson().fromJson(value, Array<Stop>::class.java).toList()
}