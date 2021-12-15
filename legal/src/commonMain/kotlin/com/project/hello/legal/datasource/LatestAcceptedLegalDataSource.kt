package com.project.hello.legal.datasource

import com.project.hello.commons.data.ResponseApi

interface LatestAcceptedLegalDataSource {
    fun fetchLatestAcceptedLegalVersion(): ResponseApi<Int>
}