package com.tneagu.realestateapp.features.listings.domain.model

import com.tneagu.realestateapp.core.domain.model.OfferType

/**
 * Domain model representing a real estate listing.
 */
data class Listing(
    val id: Int,
    val bedrooms: Int?,
    val city: String,
    val area: Double,
    val imageUrl: String?,
    val price: Double,
    val professional: String,
    val propertyType: String,
    val offerType: OfferType,
    val rooms: Int?,
)
