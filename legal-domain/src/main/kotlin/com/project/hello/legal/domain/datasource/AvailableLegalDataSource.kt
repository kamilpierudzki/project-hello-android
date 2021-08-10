package com.project.hello.legal.domain.datasource

import com.project.hallo.commons.domain.repository.Response
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi

interface AvailableLegalDataSource {
    fun fetchAvailableLegal(): Response<LatestAvailableLegalApi>
}