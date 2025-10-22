package com.tneagu.realestateapp.features.listingdetails.data.converter

import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.features.listingdetails.data.dto.ListingDetailDto
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail
import javax.inject.Inject

/**
 * Converter class to transform [ListingDetailDto] to [ListingDetail] domain model.
 */
internal class ListingDetailConverter
    @Inject
    constructor() {
        /**
         * Converts a [ListingDetailDto] to a [ListingDetail] domain model.
         *
         * @param dto The DTO from the API response.
         * @return [ListingDetail] domain model.
         */
        fun convert(dto: ListingDetailDto): ListingDetail {
            return ListingDetail(
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
