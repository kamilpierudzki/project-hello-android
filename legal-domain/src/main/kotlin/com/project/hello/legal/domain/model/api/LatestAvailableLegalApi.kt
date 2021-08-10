package com.project.hello.legal.domain.model.api

import com.project.hello.legal.domain.model.LatestAvailableLegal

data class LatestAvailableLegalApi(val version: Int, val message: String)

fun LatestAvailableLegalApi.toLatestAvailableLegal() =
    LatestAvailableLegal(version = version, message = message)
