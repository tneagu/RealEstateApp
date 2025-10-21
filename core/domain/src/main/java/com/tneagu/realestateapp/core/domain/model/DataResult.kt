package com.tneagu.realestateapp.core.domain.model

/**
 * A sealed interface representing the result of a domain operation.
 * Used across all features for consistent result handling.
 *
 * @param T The type of data on success.
 */
sealed interface DataResult<out T> {

    /**
     * Represents a successful operation with data.
     */
    data class Success<T>(val data: T) : DataResult<T>

    /**
     * Represents a failed operation with a domain error.
     */
    data class Failure(val error: DomainError) : DataResult<Nothing>
}
