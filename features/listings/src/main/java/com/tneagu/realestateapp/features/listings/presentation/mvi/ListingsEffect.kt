package com.tneagu.realestateapp.features.listings.presentation.mvi

/**
 * Represents one-time side effects for the listings screen.
 * These are events that should be consumed once by the UI (e.g., showing a snackbar, navigation).
 */
sealed interface ListingsEffect {

    /**
     * Effect to show an error message to the user.
     *
     * @param message The error message to display.
     */
    data class ShowError(val message: String) : ListingsEffect

    /**
     * Effect to navigate to the listing details screen.
     *
     * @param listingId The ID of the listing to show details for.
     */
    data class NavigateToDetails(val listingId: Int) : ListingsEffect
}
