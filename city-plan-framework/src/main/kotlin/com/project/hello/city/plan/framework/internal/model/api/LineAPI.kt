package com.project.hello.city.plan.framework.internal.model.api

import androidx.annotation.Keep
import com.project.hello.city.plan.domain.model.Line

@Keep
data class LineAPI(val number: String, val destination: String)

fun LineAPI.toLine() = Line(number = number, destination = destination)