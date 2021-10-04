package com.project.hello.transit.agency.framework.internal.model.api

import androidx.annotation.Keep
import com.project.hello.transit.agency.domain.model.Stop

@Keep
data class StopAPI(val stopName: String, val lines: List<String>)

fun StopAPI.toStop() = Stop(stopName = stopName, lines = lines)