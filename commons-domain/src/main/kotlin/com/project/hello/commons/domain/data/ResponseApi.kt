package com.project.hello.commons.domain.data

sealed class ResponseApi<T>(val data: T? = null) {
    class Success<T>(data: T) : ResponseApi<T>(data) {
        val successData: T get() = data!!
    }
    class Error<T>(val rawErrorMessage: String) : ResponseApi<T>() {
        var localisedErrorMessage: String = ""
    }
}