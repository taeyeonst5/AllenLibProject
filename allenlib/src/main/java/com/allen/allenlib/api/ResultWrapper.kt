package com.allen.allenlib.api


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null) :
        ResultWrapper<Nothing>()

    data class NetworkError(val throwable :String) : ResultWrapper<Nothing>()
}