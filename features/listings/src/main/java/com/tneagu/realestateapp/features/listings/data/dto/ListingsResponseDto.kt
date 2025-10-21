package com.tneagu.realestateapp.features.listings.data.dto

import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object for the listings API response.
 */
@JsonClass(generateAdapter = true)
internal data class ListingsResponseDto(
    val items: List<ListingDto>,
    val totalCount: Int
)
