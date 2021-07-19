package com.project.hallo.commons.domain.repository

sealed class Response<T>(
    val data: T? = null
) {
    class Success<T>(data: T) : Response<T>(data) {
        val successData: T get() = data!!
    }

    class Loading<T>(data: T? = null) : Response<T>(data)

    class Error<T>(var errorMessage: String, data: T? = null) : Response<T>(data)
}