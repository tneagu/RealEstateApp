package com.tneagu.realestateapp.features.listings.data.converter

import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.features.listings.data.dto.ListingDto
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import javax.inject.Inject

/**
 * Converter class to transform [ListingDto] to [Listing] domain model.
 */
internal class ListingConverter
    @Inject
    constructor() {
        /**
         * Converts a [ListingDto] to a [Listing] domain model.
         *
         * @param dto The DTO from the API response.
         * @return [Listing] domain model.
         */
        fun convert(dto: ListingDto): Listing {
            return Listing(
                id = dto.id,
                bedrooms = dto.bedrooms,
                city = dto.city,
                area = dto.area,
                imageUrl = dto.url,
                price = dto.price,
                professional = dto.professional,
                propertyType = dto.propertyType,
                offerType = OfferType.fromInt(dto.offerType),
                rooms = dto.rooms,
            )
        }
    }
