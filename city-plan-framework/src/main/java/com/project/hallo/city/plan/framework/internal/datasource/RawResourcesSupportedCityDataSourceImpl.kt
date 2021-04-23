package com.project.hallo.city.plan.framework.internal.datasource

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.project.hallo.city.plan.domain.datasource.SupportedCityDataSource
import com.project.hallo.city.plan.domain.model.api.SupportedCitiesApi
import com.project.hallo.city.plan.framework.R
import com.project.hallo.commons.domain.repository.ApiResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

internal class RawResourcesSupportedCityDataSourceImpl @Inject constructor(
    private val resources: Resources
) : SupportedCityDataSource {

    override fun fetchSupportedCities(): ApiResponse<SupportedCitiesApi> {
        val inputStream = resources.openRawResource(R.raw.supported_cities)
        val inputStreamReader = InputStreamReader(inputStream)

        val bufferedReader = BufferedReader(inputStreamReader)
        val json: String = bufferedReader.lines().reduce { acc, element -> acc + element }.get()
        bufferedReader.close()
        inputStreamReader.close()
        inputStream.close()

        return try {
            val supportedCitiesApi: SupportedCitiesApi? =
                Gson().fromJson(json, SupportedCitiesApi::class.java)
            if (supportedCitiesApi != null) {
                ApiResponse.Success(supportedCitiesApi)
            } else {
                ApiResponse.Error("json is empty")
            }
        } catch (exception: JsonSyntaxException) {
            ApiResponse.Error(exception.message ?: "")
        }
    }
}