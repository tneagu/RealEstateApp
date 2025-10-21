package com.tneagu.realestateapp.features.listingdetails.domain.repository

import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail

/**
 * Repository interface for fetching listing details.
 * This defines the contract for accessing detailed information about a specific listing.
 */
interface ListingDetailsRepository {

    /**
     * Fetches detailed information for a specific listing.
     *
     * @param listingId The unique identifier of the listing.
     * @return [DataResult] containing either [ListingDetail] on success or a [DomainError] on failure.
     */
    suspend fun getListingDetails(listingId: Int): DataResult<ListingDetail>
}
