package com.project.hello.legal.internal.datasource

import android.content.res.Resources
import com.google.gson.Gson
import com.project.hello.commons.data.ResponseApi
import com.project.hello.commons.resources.JsonResourceReader
import com.project.hello.legal.R
import com.project.hello.legal.datasource.AvailableLegalDataSource
import com.project.hello.legal.model.api.LatestAvailableLegalApi
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