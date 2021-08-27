package com.project.hello.commons.framework

internal object RethrowingExceptionHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        throw UncaughtException(throwable)
    }
}

internal class UncaughtException(cause: Throwable) : Exception(cause)