package com.project.hallo.vehicle.domain.steps

import com.project.hallo.city.plan.domain.Line

interface TextMatching {
    fun didNumberMatch(input: String, cityLine: Line): Boolean
    fun didDestinationMatch(input: String, cityLine: Line): Boolean
    fun didNumberContains(input: String, cityLine: Line): Boolean
    fun didDestinationContain(input: String, cityLine: Line): Boolean
    fun didSliceMatch(input: String, cityLine: Line): Boolean
}

