package com.tneagu.realestateapp.features.listingdetails.data.dto

import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object for a single listing detail from the API.
 * This represents the detailed information about a specific real estate listing.
 */
@JsonClass(generateAdapter = true)
internal data class ListingDetailDto(
    val id: Int,
    val bedrooms: Int?,
    val city: String,
    val area: Double,
    val url: String?,
    val price: Double,
    val professional: String,
    val propertyType: String,
    val offerType: Int,
    val rooms: Int?
)
