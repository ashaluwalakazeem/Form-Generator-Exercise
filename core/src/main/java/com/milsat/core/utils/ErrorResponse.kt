package com.milsat.core.utils

import androidx.compose.runtime.Stable
import com.google.gson.JsonParseException

@Stable
sealed class ErrorResponse(val message: String) {
    data class FormFieldError(val msg: String = "An unexpected error occurred while generating form fields") : ErrorResponse(msg)
    data class UnProcessableEntityError(val msg: String = "An unexpected error occurred while parsing the json file") : ErrorResponse(msg)
    class Unknown(msg: String = "An unexpected error occurred") : ErrorResponse(msg)
}


internal fun Exception.parseError(isProtectedEndpoint: Boolean = false): ErrorResponse {
    return when (this) {

        is JsonParseException -> {
            ErrorResponse.UnProcessableEntityError()
        }

        else -> ErrorResponse.Unknown()
    }
}
