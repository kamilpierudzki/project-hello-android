package com.project.hello.vehicle.domain.steps

import com.project.hello.city.plan.domain.model.Line

interface TextMatching {
    fun isNumberMatching(input: String, cityLine: Line): Boolean
    fun isNumberSliceMatching(input: String, cityLine: Line): Boolean
    fun isDestinationMatching(input: String, cityLine: Line): Boolean
    fun isDestinationSliceMatching(input: String, cityLine: Line): Boolean
}

