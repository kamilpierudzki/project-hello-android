package com.project.hello.transit.agency.framework.internal.repository.datasource

import android.content.res.Resources
import com.google.gson.Gson
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.resources.JsonResourceReader
import com.project.hello.transit.agency.framework.internal.cryptography.Decoding
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
import javax.inject.Inject

internal class LocalTransitAgencyDataSource @Inject constructor(
    resources: Resources,
    decoding: Decoding
) {

    private val jsonTransitAgencyResourceReader = JsonResourceReader(resources) { rawText ->
        val json = decoding.decode(rawText)
        Gson().fromJson(json, TransitAgencyAPI::class.java)
    }

    fun loadTransitAgency(resFile: Int): ResponseApi<TransitAgencyAPI> {
        return jsonTransitAgencyResourceReader.readFile(resFile)
    }
}