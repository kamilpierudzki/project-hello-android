package com.project.hello.transit.agency.internal.model.api

import androidx.annotation.Keep
import com.project.hello.transit.agency.model.Stop

@Keep
data class StopAPI(val stopName: String, val stopLines: List<String>)

fun StopAPI.toStop() = Stop(stopName = stopName, lines = stopLines)