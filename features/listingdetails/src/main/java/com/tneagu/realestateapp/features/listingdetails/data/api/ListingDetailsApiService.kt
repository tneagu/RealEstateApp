package com.tneagu.realestateapp.features.listingdetails.data.api

import com.tneagu.realestateapp.features.listingdetails.data.dto.ListingDetailDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API service for fetching listing details.
 */
internal interface ListingDetailsApiService {

    /**
     * Fetches detailed information for a specific listing by ID.
     *
     * @param listingId The unique identifier of the listing.
     * @return [ListingDetailDto] containing the listing details.
     */
    @GET("listings/{listingId}.json")
    suspend fun getListingDetails(@Path("listingId") listingId: Int): ListingDetailDto
}
