package com.project.hello.legal.domain.datasource

import com.project.hello.commons.domain.data.ResponseApi

interface LatestAcceptedLegalDataSource {
    fun fetchLatestAcceptedLegalVersion(): ResponseApi<Int>
}