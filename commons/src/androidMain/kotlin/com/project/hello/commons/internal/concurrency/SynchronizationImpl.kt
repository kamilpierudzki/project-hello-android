package com.project.hello.commons.internal.concurrency

import com.project.hello.commons.concurrency.Synchronization
import javax.inject.Inject

internal class SynchronizationImpl @Inject constructor() : Synchronization {

    override fun <T> synchronized(lock: Any, block: () -> T): T {
        kotlin.synchronized(lock) {
            return block.invoke()
        }
    }
}