package com.project.hello.vehicle.domain.steps

import com.project.hello.city.plan.domain.model.Line

interface MatchingCityLines {

    fun cityLinesMatchedBasedOnInput(
        input: List<String>,
        cityLines: List<Line>
    ): List<LineWithAccuracy>
}

