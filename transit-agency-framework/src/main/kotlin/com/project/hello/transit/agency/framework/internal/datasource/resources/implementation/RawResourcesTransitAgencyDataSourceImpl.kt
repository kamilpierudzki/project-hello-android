package com.project.hello.transit.agency.framework.internal.datasource.resources.implementation

import android.content.res.Resources
import com.google.gson.Gson
import com.project.hello.transit.agency.framework.internal.datasource.resources.TransitAgencyDataSource
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
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