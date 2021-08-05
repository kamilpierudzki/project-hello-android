package com.project.hallo.legal.framework.internal

import android.content.res.Resources
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.framework.resources.JsonResourceReader
import com.project.hallo.legal.framework.R
import javax.inject.Inject

internal class RawResourcesAvailableLegalDataSourceImpl @Inject constructor(
    private val resources: Resources
) : AvailableLegalDataSource {

    override fun fetchAvailableLegal(): Response<LegalApi> {
        val jsonReader = JsonResourceReader<LegalApi>(resources)
        return jsonReader.readFile(R.raw.legal_data)
    }
}