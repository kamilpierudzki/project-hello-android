package com.project.hello.vehicle.prediction.timeout.implementation

import com.project.hello.commons.time.SystemTime
import com.project.hello.vehicle.prediction.timeout.TimeoutChecker
import com.project.hello.vehicle.prediction.timeout.TimeoutCheckerFactory

class TimeoutCheckerFactoryImpl(
    private val systemTime: SystemTime,
    private val isDebugging: Boolean,
) : TimeoutCheckerFactory {

    override fun create(): TimeoutChecker {
        return if (isDebugging)
            DebugTimeoutCheckerImpl()
        else
            TimeoutCheckerImpl(systemTime)
    }
}