package com.project.hallo.city.plan.domain.model.api

import com.project.hallo.city.plan.domain.model.Line

data class LineAPI(val number: String? = null, val destination: String? = null)

fun LineAPI.toLine() = Line(number = number ?: "", destination = destination ?: "")