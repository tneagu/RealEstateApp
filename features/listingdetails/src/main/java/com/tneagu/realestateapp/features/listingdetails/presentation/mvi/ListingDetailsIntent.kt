package com.tneagu.realestateapp.features.listingdetails.presentation.mvi

/**
 * Represents user intents/actions for the listing details screen.
 * These are events triggered by user interactions or screen lifecycle.
 */
sealed interface ListingDetailsIntent {
    /**
     * Intent to load the details for a specific listing.
     * Triggered when the screen is first displayed.
     *
     * @param listingId The unique identifier of the listing to load.
     */
    data class LoadDetails(val listingId: Int) : ListingDetailsIntent

    /**
     * Intent to retry loading listing details after an error occurred.
     */
    data object Retry : ListingDetailsIntent

    /**
     * Intent to navigate back to the previous screen.
     */
    data object NavigateBack : ListingDetailsIntent
}
