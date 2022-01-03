package com.project.hello.transit.agency.model.api

import com.project.hello.transit.agency.model.Stop

data class StopAPI(val stopName: String, val stopLines: List<String>)

fun StopAPI.toStop() = Stop(stopName = stopName, lines = stopLines)