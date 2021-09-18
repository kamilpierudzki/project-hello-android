package com.project.hello.legal.framework.internal.datasource

import android.content.res.Resources
import com.google.gson.Gson
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.resources.JsonResourceReader
import com.project.hello.legal.domain.datasource.AvailableLegalDataSource
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi
import com.project.hello.legal.framework.R
import javax.inject.Inject

internal class RawResourcesLatestAvailableLegalDataSourceImpl @Inject constructor(
    resources: Resources
) : AvailableLegalDataSource {

    private val jsonResourceReader = JsonResourceReader(resources) { json ->
        Gson().fromJson(json, LatestAvailableLegalApi::class.java)
    }

    override fun fetchAvailableLegal(): ResponseApi<LatestAvailableLegalApi> {
        return jsonResourceReader.readFile(R.raw.legal_data)
    }
}