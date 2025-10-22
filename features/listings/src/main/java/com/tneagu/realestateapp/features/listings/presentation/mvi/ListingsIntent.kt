package com.tneagu.realestateapp.features.listings.presentation.mvi

/**
 * Represents user intents/actions for the listings screen.
 * These are events triggered by user interactions or screen lifecycle.
 */
sealed interface ListingsIntent {
    /**
     * Intent to load the listings from the repository.
     * Triggered when the screen is first displayed or refreshed.
     */
    data object LoadListings : ListingsIntent

    /**
     * Intent to retry loading listings after an error occurred.
     */
    data object Retry : ListingsIntent

    /**
     * Intent triggered when user clicks on a listing.
     */
    data class OnListingClick(val listingId: Int) : ListingsIntent
}
