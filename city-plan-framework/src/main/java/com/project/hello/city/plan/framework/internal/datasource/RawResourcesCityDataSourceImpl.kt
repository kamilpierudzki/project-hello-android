package com.project.hello.city.plan.framework.internal.datasource

import android.content.res.Resources
import com.project.hallo.city.plan.domain.datasource.CityDataSource
import com.project.hallo.city.plan.domain.model.api.CityPlanAPI
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.commons.framework.resources.JsonResourceReader
import javax.inject.Inject

internal class RawResourcesCityDataSourceImpl @Inject constructor(
    resources: Resources
) : CityDataSource {

    private val jsonResourceReader = JsonResourceReader<CityPlanAPI>(resources)

    override fun fetchCityData(resFile: Int): Response<CityPlanAPI> {
        return jsonResourceReader.readFile(resFile)
    }
}