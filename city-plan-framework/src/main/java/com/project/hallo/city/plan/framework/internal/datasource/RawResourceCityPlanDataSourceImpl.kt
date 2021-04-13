package com.project.hallo.city.plan.framework.internal.datasource

import android.content.res.Resources
import com.google.gson.Gson
import com.project.hallo.city.plan.domain.CityPlanAPI
import com.project.hallo.city.plan.domain.CityPlanDataSource
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.framework.R
import java.io.BufferedReader
import java.io.InputStreamReader

internal class RawResourceCityPlanDataSourceImpl(private val resources: Resources) : CityPlanDataSource {

    override fun fetchPlanFor(): CityPlanAPI {
        val inputStream = resources.openRawResource(R.raw.poznan)
        val inputStreamReader = InputStreamReader(inputStream)

        val bufferedReader = BufferedReader(inputStreamReader)
        val json: String = bufferedReader.lines().reduce { acc, element -> acc + element }.get()
        bufferedReader.close()
        inputStreamReader.close()
        inputStream.close()

        return Gson().fromJson(json, CityPlanAPI::class.java)
    }
}