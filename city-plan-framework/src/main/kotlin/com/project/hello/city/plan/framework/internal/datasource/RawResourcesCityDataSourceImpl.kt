package com.project.hello.city.plan.framework.internal.datasource

import android.content.res.Resources
import com.google.gson.reflect.TypeToken
import com.project.hello.city.plan.domain.datasource.CityDataSource
import com.project.hello.city.plan.domain.model.api.CityPlanAPI
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.resources.JsonResourceReader
import javax.inject.Inject

internal class RawResourcesCityDataSourceImpl @Inject constructor(
    resources: Resources
) : CityDataSource {

    private val jsonResourceReader = JsonResourceReader(
        resources,
        object : TypeToken<CityPlanAPI>() {},
    )

    override fun fetchCityData(resFile: Int): ResponseApi<CityPlanAPI> {
        return jsonResourceReader.readFile(resFile)
    }
}