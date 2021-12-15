package com.project.hello.vehicle.prediction.timeout.implementation

import com.project.hello.vehicle.prediction.timeout.TimeoutChecker

internal class DebugTimeoutCheckerImpl : TimeoutChecker {
    override fun isTimeout(): Boolean = false
}