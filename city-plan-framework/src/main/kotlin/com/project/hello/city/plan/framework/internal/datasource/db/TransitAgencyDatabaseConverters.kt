package com.project.hello.city.plan.framework.internal.datasource.db

import com.google.gson.Gson
import com.project.hello.transit.agency.domain.model.Line

internal object TransitAgencyDatabaseConverters {

    fun linesToString(lines: List<Line>): String =
        Gson().toJson(lines)

    fun stringToLines(value: String?): List<Line> =
        Gson().fromJson(value, Array<Line>::class.java).toList()
}