package com.tneagu.realestateapp.features.listings.domain.repository

import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.features.listings.domain.model.Listing

/**
 * Repository interface for listings data operations.
 * Defines the contract for fetching listing data.
 */
interface ListingsRepository {
    /**
     * Fetches all listings from the API.
     *
     * @return DataResult containing a list of Listing domain models or a domain error.
     */
    suspend fun getListings(): DataResult<List<Listing>>
}
