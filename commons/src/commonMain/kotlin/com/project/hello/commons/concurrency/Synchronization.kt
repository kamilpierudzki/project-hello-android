package com.project.hello.commons.concurrency

interface Synchronization {
    fun <T> synchronized(lock: Any, block: () -> T): T
}