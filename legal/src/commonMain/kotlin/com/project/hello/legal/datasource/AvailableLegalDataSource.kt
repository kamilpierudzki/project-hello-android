package com.project.hello.legal.datasource

import com.project.hello.commons.data.ResponseApi
import com.project.hello.legal.model.api.LatestAvailableLegalApi

interface AvailableLegalDataSource {
    fun fetchAvailableLegal(): ResponseApi<LatestAvailableLegalApi>
}