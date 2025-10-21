package com.tneagu.realestateapp.features.listingdetails.presentation.mvi

/**
 * Represents one-time side effects for the listing details screen.
 * These are events that should be consumed only once (e.g., navigation, toasts).
 */
sealed interface ListingDetailsEffect {

    /**
     * Effect to navigate back to the previous screen.
     */
    data object NavigateBack : ListingDetailsEffect
}
