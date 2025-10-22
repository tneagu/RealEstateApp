package com.tneagu.realestateapp.features.listings.presentation.mvi

import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.features.listings.domain.model.Listing

/**
 * Represents the UI state for the listings screen.
 * Uses sealed interface for exhaustive when expressions and type safety.
 */
sealed interface ListingsState {
    /**
     * Initial state before any data is loaded.
     */
    data object NotInitialized : ListingsState

    /**
     * Loading state while fetching listings from the repository.
     */
    data object Loading : ListingsState

    /**
     * Success state with the list of fetched listings.
     *
     * @param listings The list of listings to display.
     */
    data class Success(val listings: List<Listing>) : ListingsState

    /**
     * Error state when fetching listings fails.
     *
     * @param error The domain error that occurred.
     */
    data class Error(val error: DomainError) : ListingsState
}
