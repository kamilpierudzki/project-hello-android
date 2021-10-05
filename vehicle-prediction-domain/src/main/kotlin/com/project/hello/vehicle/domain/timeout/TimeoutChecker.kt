package com.project.hello.vehicle.domain.timeout

interface TimeoutChecker {
    fun isTimeout(): Boolean
}