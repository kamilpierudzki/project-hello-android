package com.project.hello.city.plan.framework.internal.datasource.db

import com.google.gson.Gson
import com.project.hello.city.plan.domain.model.Line

internal object CityDatabaseConverters {

    fun linesToString(lines: List<Line>): String =
        Gson().toJson(lines)

    fun stringToLines(value: String?): List<Line> =
        Gson().fromJson(value, Array<Line>::class.java).toList()
}