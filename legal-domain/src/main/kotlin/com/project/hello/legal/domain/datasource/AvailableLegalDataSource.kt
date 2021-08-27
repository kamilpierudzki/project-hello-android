package com.project.hello.legal.domain.datasource

import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi

interface AvailableLegalDataSource {
    fun fetchAvailableLegal(): ResponseApi<LatestAvailableLegalApi>
}