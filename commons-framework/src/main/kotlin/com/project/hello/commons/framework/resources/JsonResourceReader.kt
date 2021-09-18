package com.project.hello.commons.framework.resources

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.project.hello.commons.domain.data.ResponseApi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

class JsonResourceReader<T>(
    private val resources: Resources,
    private val objectCreator: (String) -> T?
) {

    fun readFile(resFile: Int): ResponseApi<T> {
        val inputStream = resources.openRawResource(resFile)
        val inputStreamReader = InputStreamReader(inputStream)

        val bufferedReader = BufferedReader(inputStreamReader)
        val json: String = bufferedReader.readText()
        bufferedReader.close()
        inputStreamReader.close()
        inputStream.close()

        return try {
            val data: T? = objectCreator.invoke(json)
            if (data != null) {
                ResponseApi.Success(data)
            } else {
                ResponseApi.Error("json is empty")
            }
        } catch (exception: JsonSyntaxException) {
            ResponseApi.Error(exception.message ?: "")
        }
    }
}