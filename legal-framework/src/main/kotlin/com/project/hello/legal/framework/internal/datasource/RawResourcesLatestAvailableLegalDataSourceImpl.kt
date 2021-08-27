package com.project.hello.legal.framework.internal.datasource

import android.content.res.Resources
import com.google.gson.reflect.TypeToken
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.resources.JsonResourceReader
import com.project.hello.legal.framework.R
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

    override fun fetchAvailableLegal(): ResponseApi<LatestAvailableLegalApi> {
        return jsonResourceReader.readFile(R.raw.legal_data)
    }
}