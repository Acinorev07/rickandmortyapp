package com.example.rickandmortyapp.data.result

sealed class Result<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null): Result<T>(data)
    class Success<T>(data: T?): Result<T>(data)
    class Error<T>(message: String, data: T? = null): Result<T>(data, message)

    data class Error2<T>(val error: ErrorEntity): Result<T>()
}
