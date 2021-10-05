package com.project.hello.vehicle.domain.timeout.implementation

import com.project.hello.vehicle.domain.timeout.TimeoutChecker
import com.project.hello.vehicle.domain.timeout.TimeoutCheckerFactory

class TimeoutCheckerFactoryImpl(private val isDebugging: Boolean) : TimeoutCheckerFactory {

    override fun create(): TimeoutChecker {
        return if (isDebugging)
            DebugTimeoutCheckerImpl()
        else
            TimeoutCheckerImpl()
    }
}