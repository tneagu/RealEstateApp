package com.tneagu.realestateapp.features.listingdetails.presentation.ui.preview

import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail

/**
 * Sample data for Compose previews.
 * Marked as internal to allow usage across all component preview files.
 */

internal val sampleListingDetailRent = ListingDetail(
    id = 1,
    bedrooms = 3,
    city = "Paris",
    area = 85.5,
    imageUrl = null,
    price = 1500.0,
    professional = "Marie Dupont - Paris Real Estate",
    propertyType = "Apartment",
    offerType = OfferType.RENT,
    rooms = 4
)

internal val sampleListingDetailSale = ListingDetail(
    id = 2,
    bedrooms = 2,
    city = "Lyon",
    area = 120.0,
    imageUrl = null,
    price = 350000.0,
    professional = "Jean Martin - Lyon Properties",
    propertyType = "House",
    offerType = OfferType.SALE,
    rooms = 5
)

internal val sampleListingDetailMinimal = ListingDetail(
    id = 3,
    bedrooms = null,
    city = "Nice",
    area = 60.0,
    imageUrl = null,
    price = 2000.0,
    professional = "Sophie Laurent",
    propertyType = "Studio",
    offerType = OfferType.RENT,
    rooms = null
)
