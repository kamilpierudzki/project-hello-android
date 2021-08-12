package com.project.hallo.legal.framework.internal.datasource

import android.content.res.Resources
import com.google.gson.reflect.TypeToken
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.framework.resources.JsonResourceReader
import com.project.hallo.legal.framework.R
import com.project.hello.legal.domain.datasource.AvailableLegalDataSource
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi
import javax.inject.Inject

internal class RawResourcesLatestAvailableLegalDataSourceImpl @Inject constructor(
    resources: Resources
) : AvailableLegalDataSource {

    private val jsonResourceReader = JsonResourceReader(
        resources,
        object : TypeToken<LatestAvailableLegalApi>() {}
    )

    override fun fetchAvailableLegal(): Response<LatestAvailableLegalApi> {
        return jsonResourceReader.readFile(R.raw.legal_data)
    }
}