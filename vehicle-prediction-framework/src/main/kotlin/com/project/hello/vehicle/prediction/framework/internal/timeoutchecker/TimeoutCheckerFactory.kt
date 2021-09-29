package com.project.hello.vehicle.prediction.framework.internal.timeoutchecker

import com.project.hello.vehicle.domain.steps.TimeoutChecker

class TimeoutCheckerFactory(private val isDebugging: Boolean) {

    fun create(): TimeoutChecker {
        return if (isDebugging)
            DebugTimeoutCheckerImpl()
        else
            TimeoutCheckerImpl()
    }
}