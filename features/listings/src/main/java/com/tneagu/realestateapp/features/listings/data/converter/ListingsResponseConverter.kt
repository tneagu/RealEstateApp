package com.tneagu.realestateapp.features.listings.data.converter

import com.tneagu.realestateapp.features.listings.data.dto.ListingsResponseDto
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import javax.inject.Inject

/**
 * Converter class to transform ListingsResponseDto to a list of Listing domain models.
 */
class ListingsResponseConverter @Inject constructor(
    private val listingConverter: ListingConverter
) {

    /**
     * Converts a ListingsResponseDto to a list of Listing domain models.
     *
     * @param dto The DTO from the API response containing a list of listings.
     * @return List of Listing domain models.
     */
    fun convert(dto: ListingsResponseDto): List<Listing> {
        return dto.items.map { listingConverter.convert(it) }
    }
}
