package com.project.hello.transit.agency.framework.internal.model.api

import androidx.annotation.Keep
import com.project.hello.transit.agency.domain.model.Line

@Keep
data class LineAPI(val number: String, val destination: String)

fun LineAPI.toLine() = Line(number = number, destination = destination)