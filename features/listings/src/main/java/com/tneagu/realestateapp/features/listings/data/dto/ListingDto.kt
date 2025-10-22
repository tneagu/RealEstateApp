package com.tneagu.realestateapp.features.listings.data.dto

import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object for a single listing from the API.
 */
@JsonClass(generateAdapter = true)
internal data class ListingDto(
    val id: Int,
    val bedrooms: Int?,
    val city: String,
    val area: Double,
    val url: String?,
    val price: Double,
    val professional: String,
    val propertyType: String,
    val offerType: Int,
    val rooms: Int?,
)
