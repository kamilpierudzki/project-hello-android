package com.project.hello.commons.data

sealed class Response<T>(
    val data: T? = null
) {
    class Success<T>(data: T) : Response<T>(data) {
        val successData: T get() = data!!
    }

    class Loading<T>(data: T? = null) : Response<T>(data)

    class Error<T>(val rawErrorMessage: String) : Response<T>() {
        var localisedErrorMessage: String = ""
    }
}

fun <T> T.toSuccessResponse(): Response.Success<T> = Response.Success(this)