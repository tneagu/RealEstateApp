package com.tneagu.realestateapp.features.listingdetails.domain.model

import com.tneagu.realestateapp.core.domain.model.OfferType

/**
 * Domain model representing detailed information about a specific real estate listing.
 */
data class ListingDetail(
    val id: Int,
    val bedrooms: Int?,
    val city: String,
    val area: Double,
    val imageUrl: String?,
    val price: Double,
    val professional: String,
    val propertyType: String,
    val offerType: OfferType,
    val rooms: Int?
)
