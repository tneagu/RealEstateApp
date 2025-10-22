package com.tneagu.realestateapp.features.listingdetails.presentation.mvi

import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail

/**
 * Represents the UI state for the listing details screen.
 * Uses sealed interface for exhaustive when expressions and type safety.
 */
sealed interface ListingDetailsState {
    /**
     * Initial state before any data is loaded.
     */
    data object NotInitialized : ListingDetailsState

    /**
     * Loading state while fetching listing details from the repository.
     */
    data object Loading : ListingDetailsState

    /**
     * Success state with the fetched listing details.
     *
     * @param detail The listing details to display.
     */
    data class Success(val detail: ListingDetail) : ListingDetailsState

    /**
     * Error state when fetching listing details fails.
     *
     * @param error The domain error that occurred.
     */
    data class Error(val error: DomainError) : ListingDetailsState
}
