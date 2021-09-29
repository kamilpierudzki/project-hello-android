package com.project.hello.vehicle.prediction.framework.internal.timeoutchecker

import com.project.hello.vehicle.domain.steps.TimeoutChecker

internal class DebugTimeoutCheckerImpl : TimeoutChecker {
    override fun isTimeout(): Boolean = false
}