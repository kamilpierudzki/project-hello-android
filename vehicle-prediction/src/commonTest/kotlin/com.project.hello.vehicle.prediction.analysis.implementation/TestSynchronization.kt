package com.project.hello.vehicle.prediction.analysis.implementation

import com.project.hello.commons.concurrency.Synchronization

internal class TestSynchronization : Synchronization {
    override fun <T> synchronized(lock: Any, block: () -> T): T {
        return block()
    }
}