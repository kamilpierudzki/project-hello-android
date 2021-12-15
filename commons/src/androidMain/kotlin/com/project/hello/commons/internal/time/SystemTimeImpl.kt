package com.project.hello.commons.internal.time

import com.project.hello.commons.time.SystemTime
import javax.inject.Inject

internal class SystemTimeImpl @Inject constructor() : SystemTime {

    override val currentTimeMillis: Long
        get() = System.currentTimeMillis()
}