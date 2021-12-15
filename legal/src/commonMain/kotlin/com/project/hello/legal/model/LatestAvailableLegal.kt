package com.project.hello.legal.model

import com.project.hello.legal.model.api.LatestAvailableLegalApi

data class LatestAvailableLegal(val version: Int, val message: String)

fun LatestAvailableLegal.toLatestAvailableLegalApi() =
    LatestAvailableLegalApi(version = version, message = message)