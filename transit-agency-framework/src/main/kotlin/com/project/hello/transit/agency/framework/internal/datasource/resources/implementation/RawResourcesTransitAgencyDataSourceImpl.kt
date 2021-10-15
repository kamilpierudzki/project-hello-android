package com.project.hello.transit.agency.framework.internal.datasource.resources.implementation

import android.content.res.Resources
import com.google.gson.Gson
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.resources.JsonResourceReader
import com.project.hello.transit.agency.framework.internal.cryptography.Decoding
import com.project.hello.transit.agency.framework.internal.datasource.resources.TransitAgencyDataSource
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
import javax.inject.Inject

internal class RawResourcesTransitAgencyDataSourceImpl @Inject constructor(
    resources: Resources,
    decoding: Decoding
) : TransitAgencyDataSource {

    private val jsonTransitAgencyResourceReader = JsonResourceReader(resources) { rawText ->
        val json = decoding.decode(rawText)
        Gson().fromJson(json, TransitAgencyAPI::class.java)
    }

    override fun fetchTransitAgencyData(resFile: Int): ResponseApi<TransitAgencyAPI> {
        return jsonTransitAgencyResourceReader.readFile(resFile)
    }
}