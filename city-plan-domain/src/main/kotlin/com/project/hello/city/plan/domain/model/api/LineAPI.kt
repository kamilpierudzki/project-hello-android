package com.project.hello.city.plan.domain.model.api

import androidx.annotation.Keep
import com.project.hello.city.plan.domain.model.Line

@Keep
data class LineAPI(val number: String? = null, val destination: String? = null)

fun LineAPI.toLine() = Line(number = number ?: "", destination = destination ?: "")