package com.project.hello.vehicle.domain.steps

import com.project.hello.city.plan.domain.model.Line

interface TextMatching {
    fun didNumberMatch(input: String, cityLine: Line): Boolean
    fun didDestinationMatch(input: String, cityLine: Line): Boolean
    fun didNumberContains(input: String, cityLine: Line): Boolean
    fun didDestinationContain(input: String, cityLine: Line): Boolean
    fun didSliceMatch(input: String, cityLine: Line): Boolean
}

