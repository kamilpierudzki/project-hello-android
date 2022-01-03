package com.project.hello.transit.agency.model.api

import com.project.hello.transit.agency.model.Line

data class LineAPI(val number: String, val destination: String)

fun LineAPI.toLine() = Line(number = number, destination = destination)