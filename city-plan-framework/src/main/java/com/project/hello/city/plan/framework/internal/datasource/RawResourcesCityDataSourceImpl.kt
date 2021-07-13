package com.project.hello.city.plan.framework.internal.datasource

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.project.hallo.city.plan.domain.datasource.CityDataSource
import com.project.hallo.city.plan.domain.model.api.CityPlanAPI
import com.project.hallo.commons.domain.repository.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

internal class RawResourcesCityDataSourceImpl @Inject constructor(
    private val resources: Resources
) : CityDataSource {

    override fun fetchCityData(resFile: Int): Response<CityPlanAPI> {
        val inputStream = resources.openRawResource(resFile)
        val inputStreamReader = InputStreamReader(inputStream)

        val bufferedReader = BufferedReader(inputStreamReader)
        val json: String = bufferedReader.lines().reduce { acc, element -> acc + element }.get()
        bufferedReader.close()
        inputStreamReader.close()
        inputStream.close()

        return try {
            val cityApi: CityPlanAPI? =
                Gson().fromJson(json, CityPlanAPI::class.java)
            if (cityApi != null) {
                Response.Success(cityApi)
            } else {
                Response.Error("json is empty")
            }
        } catch (exception: JsonSyntaxException) {
            Response.Error(exception.message ?: "")
        }
    }
}