package com.tneagu.realestateapp.features.listings.data.api

import com.tneagu.realestateapp.features.listings.data.dto.ListingsResponseDto
import retrofit2.http.GET

/**
 * Retrofit API service for listings endpoints.
 */
internal interface ListingsApiService {

    @GET("listings.json")
    suspend fun getListings(): ListingsResponseDto
}
