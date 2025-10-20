package com.tneagu.realestateapp.features.listings.domain.repository

import com.tneagu.realestateapp.core.network.model.ApiResult
import com.tneagu.realestateapp.features.listings.domain.model.Listing

/**
 * Repository interface for listings data operations.
 * Defines the contract for fetching listing data.
 */
interface ListingsRepository {

    /**
     * Fetches all listings from the API.
     *
     * @return ApiResult containing a list of Listing domain models or an error.
     */
    suspend fun getListings(): ApiResult<List<Listing>>
}
