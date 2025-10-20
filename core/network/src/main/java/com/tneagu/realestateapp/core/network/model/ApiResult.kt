package com.tneagu.realestateapp.core.network.model

/**
 * Sealed class representing the result of an API call.
 * Wraps network responses with success or error states.
 */
sealed class ApiResult<out T> {
    /**
     * Successful API response with data.
     */
    data class Success<T>(val data: T) : ApiResult<T>()

    /**
     * API call failed with an error.
     */
    data class Error(val error: NetworkError) : ApiResult<Nothing>()
}
