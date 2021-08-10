package com.project.hallo.legal.framework.internal.datasource

import android.content.res.Resources
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.framework.resources.JsonResourceReader
import com.project.hallo.legal.framework.R
import com.project.hello.legal.domain.datasource.AvailableLegalDataSource
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi
import javax.inject.Inject

internal class RawResourcesAvailableLegalDataSourceImpl @Inject constructor(
    private val resources: Resources
) : AvailableLegalDataSource {

    override fun fetchAvailableLegal(): Response<LatestAvailableLegalApi> {
        val jsonReader = JsonResourceReader<LatestAvailableLegalApi>(resources)
        return jsonReader.readFile(R.raw.legal_data)
    }
}