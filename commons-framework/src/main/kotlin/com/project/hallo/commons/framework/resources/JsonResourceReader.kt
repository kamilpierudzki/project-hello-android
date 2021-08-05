package com.project.hallo.commons.framework.resources

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.project.hallo.commons.domain.repository.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

class JsonResourceReader<T>(
    private val resources: Resources
) {

    fun readFile(resFile: Int): Response<T> {
        val inputStream = resources.openRawResource(resFile)
        val inputStreamReader = InputStreamReader(inputStream)

        val bufferedReader = BufferedReader(inputStreamReader)
        val json: String = bufferedReader.readText()
        bufferedReader.close()
        inputStreamReader.close()
        inputStream.close()

        return try {
            val fooType: Type = object : TypeToken<JsonResourceReader<T?>?>() {}.type
            val cityApi: T? = Gson().fromJson(json, fooType)
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