package com.project.hello.vehicle.domain.timeout.implementation

import com.project.hello.vehicle.domain.timeout.TimeoutChecker

internal class DebugTimeoutCheckerImpl : TimeoutChecker {
    override fun isTimeout(): Boolean = false
}