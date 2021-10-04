package com.project.hello.city.plan.framework.internal.datasource

import android.content.res.Resources
import com.google.gson.Gson
import com.project.hello.city.plan.domain.datasource.TransitAgencyDataSource
import com.project.hello.city.plan.domain.model.api.TransitAgencyAPI
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.resources.JsonResourceReader
import javax.inject.Inject

internal class RawResourcesTransitAgencyDataSourceImpl @Inject constructor(
    resources: Resources
) : TransitAgencyDataSource {

    private val jsonResourceReader = JsonResourceReader(resources) { json ->
        Gson().fromJson(json, TransitAgencyAPI::class.java)
    }

    override fun fetchTransitAgencyData(resFile: Int): ResponseApi<TransitAgencyAPI> {
        return jsonResourceReader.readFile(resFile)
    }
}