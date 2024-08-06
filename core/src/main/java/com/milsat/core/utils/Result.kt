package com.milsat.core.utils

sealed class Result<out T> {
    data class Success<T>(val data: T, val msg: String = "Success") : Result<T>()
    data class Error(val cause: ErrorResponse) : Result<Nothing>()
}
