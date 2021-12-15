package com.project.hello.legal.model.api

import com.project.hello.legal.model.LatestAvailableLegal

data class LatestAvailableLegalApi(val version: Int, val message: String)

fun LatestAvailableLegalApi.toLatestAvailableLegal() =
    LatestAvailableLegal(version = version, message = message)
