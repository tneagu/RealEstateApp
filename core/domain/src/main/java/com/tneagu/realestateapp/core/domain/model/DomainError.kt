package com.tneagu.realestateapp.core.domain.model

/**
 * A sealed interface representing domain-level errors.
 * Features can extend this with feature-specific errors.
 */
sealed interface DomainError {
    val message: String?

    /**
     * Network is unavailable (no internet connection, timeout, etc.).
     */
    data class NetworkUnavailable(
        override val message: String? = null
    ) : DomainError

    /**
     * Server returned an error response (4xx, 5xx).
     */
    data class ServerError(
        override val message: String? = null
    ) : DomainError

    /**
     * An unexpected error occurred.
     */
    data class UnknownError(
        override val message: String? = null
    ) : DomainError
}
