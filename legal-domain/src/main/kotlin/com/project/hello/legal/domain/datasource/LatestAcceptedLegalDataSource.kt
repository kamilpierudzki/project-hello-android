package com.project.hello.legal.domain.datasource

import com.project.hallo.commons.domain.repository.Response

interface LatestAcceptedLegalDataSource {
    fun fetchLatestAcceptedLegalVersion(): Response<Int>
}