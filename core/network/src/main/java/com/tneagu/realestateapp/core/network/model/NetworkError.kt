package com.tneagu.realestateapp.core.network.model

/**
 * Sealed class representing different types of network errors.
 * Provides type-safe error handling for network operations.
 */
sealed class NetworkError {
    /**
     * Server returned an error response (4xx, 5xx).
     */
    data class ServerError(
        val code: Int,
        val message: String
    ) : NetworkError()

    /**
     * Network connectivity issues (no internet, timeout, etc.).
     */
    data class NetworkException(
        val message: String,
        val cause: Throwable? = null
    ) : NetworkError()

    /**
     * Unexpected errors (parsing errors, unknown exceptions).
     */
    data class UnknownError(
        val message: String,
        val cause: Throwable? = null
    ) : NetworkError()
}
