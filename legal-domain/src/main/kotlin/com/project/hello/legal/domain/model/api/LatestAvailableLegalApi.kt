package com.project.hello.legal.domain.model.api

import androidx.annotation.Keep
import com.project.hello.legal.domain.model.LatestAvailableLegal

@Keep
data class LatestAvailableLegalApi(val version: Int, val message: String)

fun LatestAvailableLegalApi.toLatestAvailableLegal() =
    LatestAvailableLegal(version = version, message = message)
