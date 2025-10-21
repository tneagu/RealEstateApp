package com.tneagu.realestateapp.features.listingdetails.domain.usecase

import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail
import com.tneagu.realestateapp.features.listingdetails.domain.repository.ListingDetailsRepository
import javax.inject.Inject

/**
 * Use case for fetching detailed information about a specific listing.
 */
class GetListingDetailsUseCase @Inject constructor(
    private val repository: ListingDetailsRepository
) {

    /**
     * Fetches detailed information for a specific listing.
     *
     * @param listingId The unique identifier of the listing.
     * @return [DataResult] containing either [ListingDetail] on success or a [DomainError] on failure.
     */
    suspend operator fun invoke(listingId: Int): DataResult<ListingDetail> {
        return repository.getListingDetails(listingId)
    }
}
